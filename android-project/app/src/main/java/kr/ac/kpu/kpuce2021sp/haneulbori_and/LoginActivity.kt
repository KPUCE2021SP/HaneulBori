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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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

    }
}