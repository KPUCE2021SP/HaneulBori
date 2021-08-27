package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*



class LoginActivity : AppCompatActivity()
{
    var callbackManager = CallbackManager.Factory.create()
    var mOAuthLoginModule: OAuthLogin = OAuthLogin.getInstance()

    private val RC_SIGN_IN = 9001
    //firebase Auth
    private lateinit var firebaseAuth: FirebaseAuth
    //google client
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Google 로그인 옵션 구성. requestIdToken 및 Email 요청
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            //'R.string.default_web_client_id' 에는 본인의 클라이언트 아이디를 넣어주시면 됩니다.
            //저는 스트링을 따로 빼서 저렇게 사용했지만 스트링을 통째로 넣으셔도 됩니다.
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        //firebase auth 객체
        firebaseAuth = FirebaseAuth.getInstance()
            /* if (Firebase.auth.currentUser != null){
            startActivity(
                Intent(this, MainActivity::class.java)
            )
            finish()
        } */

        //카카오 키 해시
        var keyHash = Utility.getKeyHash(this)
        Log.d("KEY_HASH", keyHash)
        
        //로그인 버튼 (나중에 DB 구축해서 정보가 맞을 시 mainActivity로)


        //로그인 버튼 - email 로그인 (김형환)
        signInBtn.setOnClickListener {
            Firebase.auth.signInWithEmailAndPassword(idEditText.text.toString(), passwordEditText.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        startActivity(
                            Intent(this, MainActivity::class.java)
                        )
                        finish()
                    } else {
                        Toast.makeText(this, "loginFailed", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "loginFailed", Toast.LENGTH_SHORT).show()
                }
        }

        //회원가입 텍스트
        signUpText.setOnClickListener {
            var intent = Intent(applicationContext, SignupActivity::class.java)
            startActivity(intent)
        }

        //kakao sns 로그인
        kakaoBtn.setOnClickListener {
            // 로그인 공통 callback 구성
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    Log.e("TAG", "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i("TAG", "로그인 성공 ${token.accessToken}")
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(applicationContext)) {
                UserApiClient.instance.loginWithKakaoTalk(
                    applicationContext,
                    callback = callback
                )
            } else {
                UserApiClient.instance.loginWithKakaoAccount(
                    applicationContext,
                    callback = callback
                )
            }
        }

        //페이스북 로그인
        facebookBtn.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))
            // 페이스북 로그인 콜백
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) {
                        Log.d("facebookLogin", "Success")
                        var intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onCancel() {
                        Log.d("facebookLogin", "Cancel")
                    }

                    override fun onError(exception: FacebookException) {
                        Log.d("facebookLogin", "Error")
                    }
                })
        }

            // 사용자가 이미 페이스북에 로그인 했는지 확인(아직 미사용) -> 처리문 작성 필요
            //val accessToken: AccessToken = AccessToken.getCurrentAccessToken()
            //val isLoggedIn: Boolean = accessToken != null && !accessToken.isExpired

            val mOAuthLoginHandler: OAuthLoginHandler = object : OAuthLoginHandler() {
                override fun run(success: Boolean) {
                    if (success) {
                        val accessToken = mOAuthLoginModule.getAccessToken(this@LoginActivity)
                        val refreshToken = mOAuthLoginModule.getRefreshToken(this@LoginActivity)
                        val expiresAt = mOAuthLoginModule.getExpiresAt(this@LoginActivity)
                        val tokenType = mOAuthLoginModule.getTokenType(this@LoginActivity)
                        var intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        val errorCode = mOAuthLoginModule.getLastErrorCode(this@LoginActivity).code
                        val errorDesc = mOAuthLoginModule.getLastErrorDesc(this@LoginActivity)
                        Toast.makeText(
                            this@LoginActivity, "errorCode:" + errorCode
                                    + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        naverBtn.setOnClickListener {
            // 네이버 로그인
            mOAuthLoginModule.init(
                this@LoginActivity
                ,getString(R.string.naver_client_id)
                ,getString(R.string.naver_client_secret)
                ,getString(R.string.naver_client_name)
            )
            mOAuthLoginModule.startOauthLoginActivity(this, mOAuthLoginHandler)
        }

        // 구글 로그인
        googleBtn.setOnClickListener {
            signIn()
        }

    }

    //google login 함수
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        // 구글 로그인
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try { //구글 로그인 성공 -> firebase에 저장
                Log.d("TAG","hello")
                val account = task.getResult(ApiException::class.java)!!
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) { //구글 로그인 실패
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }
}