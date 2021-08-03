package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userEmail = "testtest@gmail.com"
        val userPW = "1q2w3e4r"

        Firebase.auth.signInWithEmailAndPassword(userEmail, userPW).addOnCompleteListener(this){
            if (it.isSuccessful){
                Toast.makeText(this, "login", Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(this, BookActivity::class.java)
                )
            } else {
                Toast.makeText(this, "faild", Toast.LENGTH_SHORT).show()
            }
        }

    }
}