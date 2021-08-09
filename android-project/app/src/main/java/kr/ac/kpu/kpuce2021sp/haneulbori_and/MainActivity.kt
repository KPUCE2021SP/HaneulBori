package kr.ac.kpu.kpuce2021sp.haneulbori_and


import android.app.AlertDialog
import android.app.TabActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : TabActivity()
{

    val DB: FirebaseFirestore = Firebase.firestore
    val laundryCollectionRef = DB.collection("laundry")
    val userCollectionRef = DB.collection("User")
    val laundries = arrayListOf<String>()
    val machines = arrayListOf<String>()
    var nowLaundry: String = ""

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

        laundryCollectionRef.get()
            .addOnSuccessListener {
                for (doc in it) {
                    laundries.add(doc.id.toString())
                }
                laundryList.adapter = search_activity.MyCustomAdapter(this, laundries)
                laundryList.setOnItemClickListener { parent, view, position, id ->
                    nowLaundry = parent.getItemAtPosition(position) as String
                    laundryCollectionRef.document(nowLaundry)
                        .collection("machine").get()
                        .addOnSuccessListener {
                            for (doc in it){
                                machines.add(doc.id.toString())
                            }
                            laundryList.adapter = search_activity.MyCustomAdapter(this, machines)
                            laundryList.setOnItemClickListener { pa, v, po, i ->

                                laundryCollectionRef.document(nowLaundry).collection("machine").document(machines[po]).get()
                                    .addOnSuccessListener {
                                        val dlg= AlertDialog.Builder(this@MainActivity)
                                        dlg.setIcon(R.mipmap.ic_launcher)
                                        dlg.setNegativeButton("취소",null)
                                        dlg.setPositiveButton("확인") { dialog, which ->
                                            tv.text = "내가 예약한 세탁기 번호 : 1번 / " + "시간 : "
                                        }
                                        if (!(it["state"] as Boolean)){
                                            dlg.setTitle("예약 할 수 없습니다.")
                                            dlg.setMessage("사유: ${it["reason"].toString()}")
                                            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
                                        } else {
                                            dlg.setTitle("예약 시간")
                                            dlg.setMessage("예약 시간들 리스트로 출력")
                                            Toast.makeText(this, it["reason"].toString(), Toast.LENGTH_SHORT).show()
                                        }
                                        dlg.show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
                                    }



                            }

                        }
                }
            }
            .addOnFailureListener {
                Log.w("MainActivity", "Error getting documents: ")
            }


        /* laundry1.setOnClickListener {
            var dlg=AlertDialog.Builder(this@MainActivity)
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
        } */
    }
}