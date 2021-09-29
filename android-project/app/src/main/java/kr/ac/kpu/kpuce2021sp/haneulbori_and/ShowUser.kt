package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_manager.*
import kotlinx.android.synthetic.main.activity_show_user.*
import kotlinx.android.synthetic.main.activity_show_user.rv_list
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class ShowUser : AppCompatActivity()
{
    val db = FirebaseFirestore.getInstance()
    val itemList = arrayListOf<ListLayout>()
    val adapter = ListAdapter(itemList)

    @RequiresApi(Build.VERSION_CODES.O)
    var date = LocalDate.now()
    var checkUser = arrayListOf<AllMachineUser>()
    var todayUser = arrayListOf<String>()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_user)

        rv_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_list.adapter = adapter

        var intent=intent
        var key=intent.getStringExtra("key")

        btnBack.setOnClickListener {
            finish()
        }

        // 금일 사용자
        if(key!!.equals("a"))
        {
            db.collection("User")
                .get()
                .addOnSuccessListener { result ->
                    itemList.clear()
                    for (document in result) {
                        val item = ListLayout(document["Name"] as String, document["PhoneNumber"] as String, document["bookList"] as ArrayList<String>)
                        for(i in item.bookList.indices)
                        {
                            if(item.bookList[i].contains(date.toString()))
                                itemList.add(item)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,"Fail",Toast.LENGTH_SHORT).show()
                }
        }
        else if(key.equals("b"))
        {
            db.collection("laundry").document("12floor").collection("machine")
                .get()
                .addOnSuccessListener { result ->
                    var tmpList = arrayOf<String>(
                        "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -",
                        "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -"
                    )
                    checkUser.clear()
                    for (document in result)
                    {
                        val item = AllMachineUser(document["book"] as ArrayList<String>,document["num"] as Long,document["type"] as String)
                        checkUser.add(item)
                    }
                    for (i in checkUser.indices)
                    {
                        if (checkUser[i].type == "washer")
                        {
                            tmpList[i] = checkUser[i].num.toString() + "번 세탁기"
                        }
                        else if (checkUser[i].type == "drier")
                        {
                            tmpList[i] = checkUser[i].num.toString() + "번 건조기"
                        }
                    }
                    var dlg = AlertDialog.Builder(this@ShowUser)
                    var sMachine = "1번 건조기"
                    dlg.setTitle("조회할 기기 목록입니다")
                    dlg.setSingleChoiceItems(tmpList, 0) { dialog, which ->
                        sMachine = tmpList[which]
                    }
                    dlg.setPositiveButton("확인") { dialog, which ->
                        var tmp = ""
                        if (sMachine.contains("세탁기"))
                        {
                            tmp = "w" + sMachine[0].toString()
                        } else if (sMachine.contains("건조기"))
                        {
                            tmp = "d" + sMachine[0].toString()
                        }
                        db.collection("laundry").document("12floor").collection("machine")
                            .document("" + tmp)
                            .get()
                            .addOnSuccessListener { result ->
                                todayUser.clear()
                                todayUser = result["book"] as ArrayList<String>
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(applicationContext, "Fail", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        db.collection("User")
                            .get()
                            .addOnSuccessListener { result ->
                                itemList.clear()
                                for (document in result) {
                                    val item = ListLayout(document["Name"] as String, document["PhoneNumber"] as String, document["bookList"] as ArrayList<String>)
                                    for (i in todayUser.indices)
                                    {
                                        if (todayUser[i].contains(item.number))
                                        {
                                            for(j in item.bookList.indices)
                                            {
                                                if(item.bookList[j].contains(tmp[0]))
                                                {
                                                    itemList.add(item)
                                                }
                                            }
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged()
                            }
                    }
                    dlg.setNegativeButton("취소",null)
                    dlg.show()
                }
        }

        // 다른 기간별 사용자 조회
        btnSearchOtherDay.setOnClickListener {
            db.collection("User")
                .get()
                .addOnSuccessListener { result ->
                    var dateArray=arrayOf("1주일","한 달","두 달")
                    var dlg=AlertDialog.Builder(this@ShowUser)
                    var choice="1주일"
                    dlg.setTitle("기간을 설정하세요")
                    dlg.setSingleChoiceItems(dateArray,0){dialog,which->
                        choice=dateArray[which]
                    }
                    dlg.setNegativeButton("취소",null)
                    dlg.setPositiveButton("확인"){dialog,which->
                        if(choice=="1주일")
                        {
                            var cal=Calendar.getInstance()
                            cal.time=Date()
                            var df : DateFormat=SimpleDateFormat("yyyy-MM-dd")
                            cal.add(Calendar.DATE,-7)
                            var beforeDateString=(df.format(cal.time)).toString()
                            var beforeDate=(beforeDateString[5].toString()+beforeDateString[6].toString()+beforeDateString[8].toString()+beforeDateString[9].toString()).toInt()
                            itemList.clear()
                            for (document in result)
                            {
                                val item = ListLayout(document["Name"] as String, document["PhoneNumber"] as String, document["bookList"] as ArrayList<String>)
                                for(i in item.bookList.indices)
                                {
                                    var k=item.bookList[i].toString()
                                    var tmp=(k[5].toString()+k[6].toString()+k[8].toString()+k[9].toString()).toInt()
                                    if(tmp>=beforeDate)
                                        itemList.add(item)
                                }
                                adapter.notifyDataSetChanged()
                            }
                        }
                        else if(choice=="한 달")
                        {
                            var cal=Calendar.getInstance()
                            cal.time=Date()
                            var df : DateFormat=SimpleDateFormat("yyyy-MM-dd")
                            cal.add(Calendar.MONTH,-1)
                            var beforeDateString=(df.format(cal.time)).toString()
                            var beforeDate=(beforeDateString[5].toString()+beforeDateString[6].toString()+beforeDateString[8].toString()+beforeDateString[9].toString()).toInt()
                            itemList.clear()
                            for (document in result)
                            {
                                val item = ListLayout(document["Name"] as String, document["PhoneNumber"] as String, document["bookList"] as ArrayList<String>)
                                for(i in item.bookList.indices)
                                {
                                    var k=item.bookList[i].toString()
                                    var tmp=(k[5].toString()+k[6].toString()+k[8].toString()+k[9].toString()).toInt()
                                    if(tmp>=beforeDate)
                                        itemList.add(item)
                                }
                                adapter.notifyDataSetChanged()
                            }
                        }
                        else if(choice=="두 달")
                        {
                            var cal=Calendar.getInstance()
                            cal.time=Date()
                            var df : DateFormat=SimpleDateFormat("yyyy-MM-dd")
                            cal.add(Calendar.MONTH,-2)
                            var beforeDateString=(df.format(cal.time)).toString()
                            var beforeDate=(beforeDateString[5].toString()+beforeDateString[6].toString()+beforeDateString[8].toString()+beforeDateString[9].toString()).toInt()
                            itemList.clear()
                            for (document in result)
                            {
                                val item = ListLayout(document["Name"] as String, document["PhoneNumber"] as String, document["bookList"] as ArrayList<String>)
                                for(i in item.bookList.indices)
                                {
                                    var k=item.bookList[i].toString()
                                    var tmp=(k[5].toString()+k[6].toString()+k[8].toString()+k[9].toString()).toInt()
                                    if(tmp>=beforeDate)
                                        itemList.add(item)
                                }
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                    dlg.show()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,"Fail",Toast.LENGTH_SHORT).show()
                }
        }

        // 다른 기기별 사용자 조회
        btnSearchOtherUser.setOnClickListener {
            db.collection("laundry").document("12floor").collection("machine")
                .get()
                .addOnSuccessListener { result ->
                    var tmpList = arrayOf<String>(
                        "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -",
                        "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -")
                    checkUser.clear()
                    for (document in result)
                    {
                        val item = AllMachineUser(document["book"] as ArrayList<String>,document["num"] as Long,document["type"] as String)
                        checkUser.add(item)
                    }
                    for (i in checkUser.indices)
                    {
                        if (checkUser[i].type == "washer")
                        {
                            tmpList[i] = checkUser[i].num.toString() + "번 세탁기"
                        }
                        else if (checkUser[i].type == "drier")
                        {
                            tmpList[i] = checkUser[i].num.toString() + "번 건조기"
                        }
                    }
                    var dlg = AlertDialog.Builder(this@ShowUser)
                    var sMachine = "1번 건조기"
                    dlg.setTitle("조회할 기기 목록입니다")
                    dlg.setSingleChoiceItems(tmpList, 0) { dialog, which ->
                        sMachine = tmpList[which]
                    }
                    dlg.setPositiveButton("확인") { dialog, which ->
                        var tmp = ""
                        if (sMachine.contains("세탁기"))
                        {
                            tmp = "w" + sMachine[0].toString()
                        }
                        else if (sMachine.contains("건조기"))
                        {
                            tmp = "d" + sMachine[0].toString()
                        }
                        db.collection("laundry").document("12floor").collection("machine")
                            .document("" + tmp)
                            .get()
                            .addOnSuccessListener { result ->
                                todayUser.clear()
                                todayUser = result["book"] as ArrayList<String>
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(applicationContext, "Fail", Toast.LENGTH_SHORT).show()
                            }
                        db.collection("User")
                            .get()
                            .addOnSuccessListener { result ->
                                itemList.clear()
                                for (document in result)
                                {
                                    val item = ListLayout(document["Name"] as String, document["PhoneNumber"] as String, document["bookList"] as ArrayList<String>)
                                    for (i in todayUser.indices)
                                    {
                                        if (todayUser[i].contains(item.number))
                                        {
                                            for(j in item.bookList.indices)
                                            {
                                                if(item.bookList[j].contains(tmp[0]))
                                                {
                                                    itemList.add(item)
                                                }
                                            }
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged()
                            }
                    }
                    dlg.setNegativeButton("취소",null)
                    dlg.show()
                }
        }
    }
}