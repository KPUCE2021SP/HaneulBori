package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.laundryList

class ChooseActivity : AppCompatActivity() {

    val DB: FirebaseFirestore = Firebase.firestore
    val laundryCollectionRef = DB.collection("laundry")
    val washer = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        laundryCollectionRef.document("11floor").collection("machine").get()
            .addOnSuccessListener {
                for (doc in it){
                    washer.add(doc.id.toString())
                }
                laundryList.adapter = search_activity.MyCustomAdapter(this, washer)
                laundryList.setOnItemClickListener { parent, view, position, id ->
                    var dlg= AlertDialog.Builder(this@ChooseActivity)
                    dlg.setTitle("예약 시간")
                    dlg.setMessage("예약 시간들 리스트로 출력")
                    dlg.setIcon(R.mipmap.ic_launcher)
                    dlg.setNegativeButton("취소",null)
                    dlg.setPositiveButton("확인"){dialog,which->

                        tv.text="내가 예약한 세탁기 번호 : 1번 / "+"시간 : "

                    }
                    dlg.show()
                }


            }
            .addOnFailureListener {
                Log.w("MainActivity", "Error getting documents: ")
            }


    }
}