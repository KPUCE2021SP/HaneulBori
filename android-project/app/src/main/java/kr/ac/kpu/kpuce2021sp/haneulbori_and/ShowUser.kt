package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.app.AlertDialog
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
import java.time.LocalDate

class ShowUser : AppCompatActivity()
{
    val db = FirebaseFirestore.getInstance()
    val itemList = arrayListOf<ListLayout>()
    var machineList = arrayListOf<MachineList>()
    var statusList = arrayListOf<StatusList>()
    val adapter = ListAdapter(itemList)

    @RequiresApi(Build.VERSION_CODES.O)
    var date = LocalDate.now()
    var errorList = arrayListOf<ErrorList>()
    var realList = arrayListOf<String>()
    var checkUser = arrayListOf<AllMachineUser>()
    var washer1User = arrayListOf<MachineUser>()
    var washer2User = arrayListOf<MachineUser>()
    var drier1User = arrayListOf<MachineUser>()
    var drier2User = arrayListOf<MachineUser>()

    //var todayUser=arrayListOf<MachineUser>()
    var todayUser = arrayListOf<String>()
    var washerCnt = 0
    var drierCnt = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_user)


        btnBack.setOnClickListener {
            finish()
        }

        btnSearchOtherUser.setOnClickListener {
            db.collection("laundry").document("12floor").collection("machine")
                .get()
                .addOnSuccessListener { result ->
                    var tmpList = arrayOf<String>(
                        "-     -     -     -     -",
                        "-     -     -     -     -",
                        "-     -     -     -     -",
                        "-     -     -     -     -",
                        "-     -     -     -     -",
                        "-     -     -     -     -",
                        "-     -     -     -     -",
                        "-     -     -     -     -"
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
                                    val item = ListLayout(
                                        document["Name"] as String, document["PhoneNumber"] as String, document["bookList"] as ArrayList<String>
                                    )
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

        rv_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_list.adapter = adapter

        db.collection("laundry").document("12floor").collection("machine")
            .get()
            .addOnSuccessListener { result ->
                var tmpList = arrayOf<String>(
                    "-     -     -     -     -",
                    "-     -     -     -     -",
                    "-     -     -     -     -",
                    "-     -     -     -     -",
                    "-     -     -     -     -",
                    "-     -     -     -     -",
                    "-     -     -     -     -",
                    "-     -     -     -     -"
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
                                val item = ListLayout(
                                    document["Name"] as String,
                                    document["PhoneNumber"] as String,
                                    document["bookList"] as ArrayList<String>
                                )
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
