package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.app.AlertDialog
import android.app.TabActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : TabActivity()
{
    companion object
    {
        var laundry1_reserved=false
        var laundry2_reserved=false
        var laundry3_reserved=false
        var laundry4_reserved=false
    }
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


        laundry1.setOnClickListener {
            var dlg= AlertDialog.Builder(this@MainActivity)
            dlg.setTitle("예약 시간")
            dlg.setMessage("예약 시간들 리스트로 출력")
            dlg.setIcon(R.mipmap.ic_launcher)
            dlg.setNegativeButton("취소",null)
            dlg.setPositiveButton("확인"){dialog,which->
                laundry1_reserved=true
                tv.text="내가 예약한 세탁기 번호 : 1번 / "+"시간 : "

            }
            dlg.show()
        }
        laundry2.setOnClickListener {
            var dlg=AlertDialog.Builder(this@MainActivity)
            dlg.setTitle("예약 시간")
            dlg.setMessage("예약 시간들 리스트로 출력")
            dlg.setIcon(R.mipmap.ic_launcher)
            dlg.setNegativeButton("취소",null)
            dlg.setPositiveButton("확인"){dialog,which->
                laundry2_reserved=true
                tv.text="내가 예약한 세탁기 번호 : 2번 / "+"시간 : "
            }
            dlg.show()
        }
        laundry3.setOnClickListener {
            var dlg=AlertDialog.Builder(this@MainActivity)
            dlg.setTitle("예약 시간")
            dlg.setMessage("예약 시간들 리스트로 출력")
            dlg.setIcon(R.mipmap.ic_launcher)
            dlg.setNegativeButton("취소",null)
            dlg.setPositiveButton("확인"){dialog,which->
                laundry3_reserved=true
                tv.text="내가 예약한 세탁기 번호 : 3번 / "+"시간 : "
            }
            dlg.show()
        }
        laundry4.setOnClickListener {
            var dlg=AlertDialog.Builder(this@MainActivity)
            dlg.setTitle("예약 시간")
            dlg.setMessage("예약 시간들 리스트로 출력")
            dlg.setIcon(R.mipmap.ic_launcher)
            dlg.setNegativeButton("취소",null)
            dlg.setPositiveButton("확인"){dialog,which->
                laundry4_reserved=true
                tv.text="내가 예약한 세탁기 번호 : 4번 / "+"시간 : "
            }
            dlg.show()
        }




    }
}