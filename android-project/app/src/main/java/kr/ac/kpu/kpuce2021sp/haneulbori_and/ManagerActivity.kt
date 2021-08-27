package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.app.Activity
import android.app.ActivityManager
import android.app.AlertDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ColorStateListInflaterCompat.inflate
import androidx.core.content.res.ComplexColorCompat.inflate
import androidx.core.graphics.drawable.DrawableCompat.inflate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_manager.*
import java.nio.file.Files.size
import java.time.LocalDate
import kotlin.properties.Delegates
import kotlin.collections.hashMapOf as hashMapOf

class ManagerActivity : AppCompatActivity()
{
    val db = FirebaseFirestore.getInstance()
    val itemList = arrayListOf<ListLayout>()
    var machineList = arrayListOf<MachineList>()
    var statusList = arrayListOf<StatusList>()
    val adapter = ListAdapter(itemList)
    @RequiresApi(Build.VERSION_CODES.O)
    var date= LocalDate.now()
    var errorList = arrayListOf<ErrorList>()
    var realList= arrayListOf<String>()
    var washerCnt=0
    var drierCnt=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)

        rv_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_list.adapter = adapter

        db.collection("laundry").document("12floor").collection("machine")
            .get()
            .addOnSuccessListener { result ->
                realList.clear()
                for (document in result)
                {
                    val item = MachineList(document["num"] as Long, document["type"] as String)
                    var tmp=""
                    if(item.type=="washer")
                    {
                        tmp="w"+item.num.toString()
                        washerCnt++
                    }
                    else if(item.type=="drier")
                    {
                        tmp="d"+item.num.toString()
                        drierCnt++
                    }
                    realList.add(tmp)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext,"Fail",Toast.LENGTH_SHORT).show()
            }

        //전체 사용자 출력
        btnReadAll.setOnClickListener {
            db.collection("User")
                .get()
                .addOnSuccessListener { result ->
                    itemList.clear()
                    for (document in result)
                    {
                        val item = ListLayout(document["Birthday"] as String, document["Email"] as String, document["Name"] as String, document["PhoneNumber"] as String, document["Sex"] as String, document["bookList"] as ArrayList<String>)
                        itemList.add(item)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,"Fail",Toast.LENGTH_SHORT).show()
                }
        }

        //남자 사용자 출력
        btnReadM.setOnClickListener {
            db.collection("User")
                .get()
                .addOnSuccessListener { result ->
                    itemList.clear()
                    for (document in result)
                    {
                        val item = ListLayout(document["Birthday"] as String, document["Email"] as String, document["Name"] as String, document["PhoneNumber"] as String, document["Sex"] as String, document["bookList"] as ArrayList<String>)
                        if(item.sex=="M")
                            itemList.add(item)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,"Fail",Toast.LENGTH_SHORT).show()
                }
        }

        //금일 사용자 출력
        btnToday.setOnClickListener {
            db.collection("User")
                .get()
                .addOnSuccessListener { result ->
                    itemList.clear()
                    for (document in result) {
                        val item = ListLayout(document["Birthday"] as String, document["Email"] as String, document["Name"] as String, document["PhoneNumber"] as String, document["Sex"] as String, document["bookList"] as ArrayList<String>)
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

        //기기 추가
        btnAddMachine.setOnClickListener {
            var choiceMachine = arrayOf<String>("세탁기","건조기")
            var dlg=AlertDialog.Builder(this@ManagerActivity)
            var choiceItem="세탁기"
            dlg.setTitle("추가할 기기를 선택하세요")
            dlg.setSingleChoiceItems(choiceMachine,0){ dialog, which->
                choiceItem=choiceMachine[which]
            }
            dlg.setPositiveButton("추가"){dialog, which->
                if(choiceItem.equals("세탁기"))
                {
                    val data= hashMapOf(
                        "book" to ArrayList<String>(),
                        "reason" to "normal",
                        "state" to true,
                        "type" to "washer",
                        "num" to (++washerCnt)
                    )
                    var tmp="w"+(washerCnt).toString()
                    db.collection("laundry").document("12floor").collection("machine").document(tmp)
                        .set(data)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext,"세탁기 추가 완료",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext,"세탁기 추가 실패",Toast.LENGTH_SHORT).show()
                        }
                }
                else if(choiceItem.equals("건조기"))
                {
                    val data= hashMapOf(
                        "book" to ArrayList<String>(),
                        "reason" to "normal",
                        "state" to true,
                        "type" to "drier",
                        "num" to (++drierCnt)
                    )
                    var tmp="d"+(drierCnt).toString()
                    db.collection("laundry").document("12floor").collection("machine").document(tmp)
                        .set(data)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext,"건조기 추가 완료",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext,"건조기 추가 실패",Toast.LENGTH_SHORT).show()
                        }
                }
            }
            dlg.setNegativeButton("취소"){dialog,which->
                Toast.makeText(applicationContext,"취소했습니다",Toast.LENGTH_SHORT).show()
            }
            dlg.show()
        }

        //기기 제거
        btnDeleteMachine.setOnClickListener {
            db.collection("laundry").document("12floor").collection("machine")
                .get()
                .addOnSuccessListener { result ->
                    machineList.clear()
                    for (document in result)
                    {
                        val item = MachineList(document["num"] as Long, document["type"] as String)
                        machineList.add(item)
                    }
                    var selectMachine=""
                    var deleteMachine = arrayOf<String>("-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -")
                    var washerMachine = arrayListOf<MachineList>()
                    var drierMachine = arrayListOf<MachineList>()
                    var dlg=AlertDialog.Builder(this@ManagerActivity)
                    dlg.setTitle("제거할 기기를 선택하세요")

                    for(i in machineList.indices)
                    {
                        if(machineList[i].type=="washer")
                        {
                            washerMachine.add(machineList[i])
                            deleteMachine[i]=machineList[i].num.toString()+"번 세탁기"
                        }
                        else
                        {
                            drierMachine.add(machineList[i])
                            deleteMachine[i]=machineList[i].num.toString()+"번 건조기"
                        }
                    }

                    dlg.setSingleChoiceItems(deleteMachine, 0){dialog, which ->
                        selectMachine=deleteMachine[which]
                    }
                    dlg.setPositiveButton("확인"){dialog, which->
                        var tmp=""
                        if(selectMachine.contains("세탁기"))
                        {
                            tmp="w"+selectMachine[0].toString()
                            washerCnt--
                        }
                        else if(selectMachine.contains("건조기"))
                        {
                            tmp="d"+selectMachine[0].toString()
                            drierCnt--
                        }
                        db.collection("laundry").document("12floor").collection("machine").document(""+tmp)
                            .delete()
                        Toast.makeText(applicationContext,selectMachine+"을 제거했습니다",Toast.LENGTH_SHORT).show()

                    }
                    dlg.setNegativeButton("취소"){dialog, which->
                        Toast.makeText(applicationContext,"취소했습니다",Toast.LENGTH_SHORT).show()
                    }
                    dlg.show()
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,"Fail",Toast.LENGTH_SHORT).show()
                }
        }

        //기기 상태 조회
        btnSearchMachine.setOnClickListener {
            db.collection("laundry").document("12floor").collection("machine")
                .get()
                .addOnSuccessListener { result ->
                    var tmpList = arrayOf<String>("-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -")
                    statusList.clear()
                    for (document in result)
                    {
                        val item = StatusList(document["num"] as Long, document["reason"] as String, document["state"] as Boolean, document["type"] as String)
                        statusList.add(item)
                    }
                    for(i in statusList.indices)
                    {
                        if(statusList[i].type=="washer")
                        {
                            if(statusList[i].state==true)
                            {
                                if(statusList[i].reason=="working")
                                    tmpList[i]=statusList[i].num.toString()+"번 세탁기 / 상태 - 이상없음 / 작동중"
                                else
                                    tmpList[i]=statusList[i].num.toString()+"번 세탁기 / 상태 - 이상없음 / 작동중 X"

                            }
                            else if(statusList[i].state==false)
                            {
                                tmpList[i]=statusList[i].num.toString()+"번 세탁기 / 상태 - 고장("+statusList[i].reason+")"
                            }
                        }
                        else if(statusList[i].type=="drier")
                        {
                            if(statusList[i].state==true)
                            {
                                if(statusList[i].reason=="working")
                                    tmpList[i]=statusList[i].num.toString()+"번 건조기 / 상태 - 이상없음 / 작동중"
                                else
                                    tmpList[i]=statusList[i].num.toString()+"번 건조기 / 상태 - 이상없음 / 작동중 X"
                            }
                            else if(statusList[i].state==false)
                            {
                                tmpList[i]=statusList[i].num.toString()+"번 건조기 / 상태 - 고장("+statusList[i].reason+")"
                            }
                        }
                    }
                    var dlg=AlertDialog.Builder(this@ManagerActivity)
                    dlg.setTitle("기기 상태 목록입니다")
                    dlg.setItems(tmpList){dialog, which ->
                    }
                    dlg.setPositiveButton("확인",null)
                    dlg.show()
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,"조회 실패",Toast.LENGTH_SHORT).show()
                }

        }

        //고장 접수 확인
        btnCheckError.setOnClickListener {
            db.collection("Error")
                .get()
                .addOnSuccessListener { result ->
                    errorList.clear()
                    var tmpList = arrayOf<String>("-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -","-     -     -     -     -")
                    for (document in result)
                    {
                        var item=ErrorList(document["num"] as Long, document["message"] as String, document["type"] as String)
                        errorList.add(item)
                    }
                    if(errorList.isEmpty())
                    {
                        tmpList[0]="신고된 고장이 없습니다"
                        for(i in 1..7)
                            tmpList[i]=""
                    }
                    else
                    {
                        for(i in errorList.indices)
                        {
                            if(errorList[i].type=="washer")
                            {
                                tmpList[i]=errorList[i].num.toString()+"번 세탁기 고장 메세지 : "+errorList[i].message
                            }
                            else if(errorList[i].type=="drier")
                            {
                                tmpList[i]=errorList[i].num.toString()+"번 건조기 고장 메세지 : "+errorList[i].message
                            }
                        }
                    }
                    var dlg=AlertDialog.Builder(this@ManagerActivity)
                    dlg.setTitle("신고된 고장 기기와 이유들입니다")
                    dlg.setItems(tmpList){dialog, which ->
                    }
                    dlg.setPositiveButton("확인",null)
                    dlg.show()
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,"Fail",Toast.LENGTH_SHORT).show()
                }
        }
    }
}

class MachineList(var num:Long, var type:String)
class StatusList(var num:Long, var reason:String, var state:Boolean, var type:String)
class ErrorList(var num:Long, var message:String, var type:String)