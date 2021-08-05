package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_search.*

class ChooseActivity : AppCompatActivity() {

    val DB: FirebaseFirestore = Firebase.firestore
    val laundryCollectionRef = DB.collection("laundry")
    val washer = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        laundryCollectionRef.document("11floor").collection("machine").get()
            .addOnSuccessListener {
                for (doc in it){
                    washer.add(doc.id.toString())
                }
                laundryList.adapter = search_activity.MyCustomAdapter(this, washer)
                laundryList.setOnItemClickListener { parent, view, position, id ->

                }


            }
            .addOnFailureListener {
                Log.w("MainActivity", "Error getting documents: ")
            }


    }
}