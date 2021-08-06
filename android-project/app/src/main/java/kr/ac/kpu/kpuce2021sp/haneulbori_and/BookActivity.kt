package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.activity_book.view.*
import java.util.*

class BookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        Log.d("MainActivity", "Error getting documents: 3")
        bookButton.setOnClickListener {
            startActivity(
                Intent(this, search_activity::class.java)
            )
        }
    }
}