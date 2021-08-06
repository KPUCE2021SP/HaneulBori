package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.kakao.sdk.common.KakaoSdk
import com.nhn.android.naverlogin.OAuthLogin


class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)

    }

}