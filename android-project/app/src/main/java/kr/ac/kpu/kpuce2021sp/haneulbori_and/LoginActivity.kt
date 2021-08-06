package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.api.Api
import com.kakao.sdk.auth.model.OAuthToken
import kotlinx.android.synthetic.main.activity_login.*
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import android.widget.Button
import com.facebook.CallbackManager
import kotlinx.android.synthetic.main.activity_login.*
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsConstants
import com.facebook.FacebookException

import com.facebook.login.LoginResult

import com.facebook.FacebookCallback

import com.facebook.login.LoginManager
import com.facebook.AccessToken
import com.nhn.android.naverlogin.OAuthLogin
import java.util.*
import android.widget.Toast
import com.nhn.android.naverlogin.OAuthLogin.mOAuthLoginHandler

import com.nhn.android.naverlogin.OAuthLoginHandler





class LoginActivity : AppCompatActivity()
{
    var callbackManager = CallbackManager.Factory.create()
    var mOAuthLoginModule: OAuthLogin = OAuthLogin.getInstance()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        //카카오 키 해시
        var keyHash = Utility.getKeyHash(this)
        Log.d("KEY_HASH", keyHash)
        
        //로그인 버튼 (나중에 DB 구축해서 정보가 맞을 시 mainActivity로)
        signInBtn.setOnClickListener {
            var intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
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
                UserApiClient.instance.loginWithKakaoTalk(applicationContext, callback = callback)
            } else {
                UserApiClient.instance.loginWithKakaoAccount(applicationContext, callback = callback)
            }
        }

        //페이스북 로그인
        facebookBtn.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"))
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

    }

    // 로그인 결과를 callbackManager를 통해 LoginManager에 전달
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}