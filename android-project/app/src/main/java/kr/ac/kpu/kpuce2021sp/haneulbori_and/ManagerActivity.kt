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
import java.time.LocalDate
import kotlin.properties.Delegates
import kotlin.collections.hashMapOf as hashMapOf

class ManagerActivity : AppCompatActivity()
{
    val db = FirebaseFirestore.getInstance()
    val itemList = arrayListOf<ListLayout>()
    val adapter = ListAdapter(itemList)
    @RequiresApi(Build.VERSION_CODES.O)
    var date= LocalDate.now()
    //var choiceItem : Int? = null
    var errorList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)

        rv_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_list.adapter = adapter

        //전체 사용자 출력
        btnReadAll.setOnClickListener {
            db.collection("User")
                .get()
                .addOnSuccessListener { result ->
                    itemList.clear()
                    for (document in result) {
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
                    for (document in result) {
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

        //기기 삭제
        /*btnDeleteMachine.setOnClickListener {
            db.collection("laundry").document("12floor").collection("machine")
                .delete()
        }*/

        //기기 추가
        btnAddMachine.setOnClickListener {
            var choiceMachine = arrayOf("세탁기","건조기")
            var dlg=AlertDialog.Builder(this@ManagerActivity)
            dlg.setTitle("추가할 기기를 선택하세요")
            dlg.setItems(choiceMachine){ dialog, which->
                var choiceItem=which
                if(choiceItem==0)
                {
                    val data= hashMapOf(
                        "book" to ArrayList<String>(),
                        "reason" to "normal",
                        "state" to true,
                        "type" to "washer"
                    )
                    db.collection("laundry").document("12floor").collection("machine")
                        .add(data)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext,"세탁기 추가 완료",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext,"세탁기 추가 실패",Toast.LENGTH_SHORT).show()
                        }
                }
                else if(choiceItem==1)
                {
                    val data= hashMapOf(
                        "book" to ArrayList<String>(),
                        "reason" to "normal",
                        "state" to true,
                        "type" to "drier"
                    )
                    db.collection("laundry").document("12floor").collection("machine")
                        .add(data)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext,"건조기 추가 완료",Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext,"건조기 추가 실패",Toast.LENGTH_SHORT).show()
                        }
                }
            }
            dlg.setNegativeButton("취소",null)
            dlg.show()
        }

        //기기 상태 조회
        btnSearchMachine.setOnClickListener {
            db.collection("laundry").document("12floor").collection("machine")
                .get()
                .addOnSuccessListener { result ->
                    itemList.clear()
                    for (document in result) {
                        val item = ListLayout(document["Birthday"] as String, document["Email"] as String, document["Name"] as String, document["PhoneNumber"] as String, document["Sex"] as String, document["bookList"] as ArrayList<String>)
                        var state = document["reason"] as String
                        for(i in item.bookList.indices)
                        {
                            if(item.bookList[i].contains(date.toString()))
                                itemList.add(item)
                        }
                    }
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
                    for (document in result) {
                        var item1=document["errorMessage"] as String
                        errorList.add(item1)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,"Fail",Toast.LENGTH_SHORT).show()
                }
        }
    }
}