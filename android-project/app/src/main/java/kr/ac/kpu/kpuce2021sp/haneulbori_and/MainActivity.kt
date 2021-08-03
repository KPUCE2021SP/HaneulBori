package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.app.TabActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : TabActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var tabHost=this.tabHost

        var tabSpecReserve=tabHost.newTabSpec("예약하기").setIndicator("예약하기")
        tabSpecReserve.setContent(R.id.tabReserve)
        tabHost.addTab(tabSpecReserve)

        var tabSpecCancel=tabHost.newTabSpec("취소하기").setIndicator("취소하기")
        tabSpecCancel.setContent(R.id.tabCancel)
        tabHost.addTab(tabSpecCancel)


        tabHost.currentTab=0


    }
}