package kr.ac.kpu.kpuce2021sp.haneulbori_and

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.activity_book.view.*
import java.util.*

class BookActivity : AppCompatActivity() {

    val DB: FirebaseFirestore = Firebase.firestore
    val itemRef = DB.collection("laundry")

    val mdate = datepicker
    val mtime = timepicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        bookButton.setOnClickListener {
            val bookingTime = mtime.currentHour
            val bookingMin = mtime.currentMinute

            val bookingData = mdate.dayOfMonth
            val bookingMonth = mdate.month
            val bookingYear = mdate.year
        }
    }

    private fun searchBookable (){

    }
}