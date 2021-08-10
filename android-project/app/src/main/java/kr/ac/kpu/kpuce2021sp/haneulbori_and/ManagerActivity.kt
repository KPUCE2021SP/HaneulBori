package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.app.Activity
import android.app.ActivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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

class ManagerActivity : AppCompatActivity()
{
    val db = FirebaseFirestore.getInstance()
    val itemList = arrayListOf<ListLayout>()
    val adapter = ListAdapter(itemList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)

        rv_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_list.adapter = adapter

        btnRead.setOnClickListener {
            db.collection("User")
                .get()
                .addOnSuccessListener { result ->
                    itemList.clear()
                    for (document in result) {
                        val item = ListLayout(document["Birthday"] as String, document["Email"] as String, document["Name"] as String, document["PhoneNumber"] as String, document["Sex"] as String)
                        itemList.add(item)
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext,"Fail",Toast.LENGTH_SHORT).show()
                    Log.w("MainActivity", "Error getting documents: $exception")
                }
        }
    }


}