package kr.ac.kpu.kpuce2021sp.haneulbori_and


import android.app.AlertDialog
import android.app.TabActivity


import android.annotation.SuppressLint

import android.os.Build

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : TabActivity()
{

    val DB: FirebaseFirestore = Firebase.firestore
    val laundryCollectionRef = DB.collection("laundry")
    val userCollectionRef = DB.collection("User")
    val laundries = arrayListOf<String>()
    val machines = arrayListOf<String>()
    var nowLaundry: String = ""
    val user = Firebase.auth.currentUser

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
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

                                laundryCollectionRef.document(nowLaundry)
                                    .collection("machine")
                                    .document(machines[po]).get()
                                    .addOnSuccessListener {
                                        val dlg= AlertDialog.Builder(this@MainActivity)
                                        dlg.setIcon(R.mipmap.ic_launcher)

                                        if (!(it["state"] as Boolean)){
                                            dlg.setTitle("예약 할 수 없습니다.")
                                            dlg.setMessage("사유: ${it["reason"].toString()}")
                                            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
                                            dlg.setPositiveButton("확인", null)
                                        } else {
                                            val temp = it["book"] as ArrayList<*>
                                            if (true){
                                                dlg.setTitle("예약 가능 시간")
                                                dlg.setMessage("예약 내역이 없습니다. 예약하시겠습니까?")
                                                dlg.setNegativeButton("취소",null)
                                                dlg.setPositiveButton("확인") { dialog, which ->
                                                    if (user != null) {
                                                        userCollectionRef.document(user.uid).get()
                                                            .addOnSuccessListener {
                                                                if (it["bookList"] == null){
                                                                    Toast.makeText(this, "faild to access database", Toast.LENGTH_SHORT).show()
                                                                } else {
                                                                    val item = it["bookList"] as ArrayList<String>

                                                                    item.add(convertDateNow())
                                                                    DB.runTransaction {
                                                                        userCollectionRef.document(user.uid).update("bookList", item)
                                                                        laundryCollectionRef.document(nowLaundry).collection("machine")
                                                                            .document("w1").get()
                                                                            .addOnSuccessListener {
                                                                                val bookList = it["book"] as ArrayList<String>
                                                                                bookList.add(convertDateNow())
                                                                                laundryCollectionRef.document(nowLaundry).collection("machine")
                                                                                    .document("w1")
                                                                                    .update("book", bookList)
                                                                            }
                                                                    }

                                                                }
                                                            }
                                                    }

                                                    tv.text = "예약 ${it.id}"
                                                }
                                            } else {
                                                var massage: String = ""
                                                val sdf = SimpleDateFormat("yyyy-MM-dd / hh:mm")
                                                for (time in temp){
                                                    if (time == null){
                                                        break
                                                    } else {
                                                        val ttime = time as Timestamp
                                                        massage = massage + sdf.format(ttime.toDate()).toString() + "\n"
                                                    }
                                                }
                                                dlg.setTitle("예약 가능 시간")
                                                dlg.setMessage("해당 시간에 예약이 있습니다\n${massage}")
                                                dlg.setPositiveButton("확인", null)
                                            }
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


    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateNow() : String{
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    }
}