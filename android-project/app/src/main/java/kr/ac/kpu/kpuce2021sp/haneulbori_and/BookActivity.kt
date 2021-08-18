package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_book.*

class BookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)

        val nextIntent = Intent(this, MainActivity::class.java)

        bookButton.setOnClickListener {
            startActivity(
                Intent(this, search_activity::class.java)
            )
        }
    }
}