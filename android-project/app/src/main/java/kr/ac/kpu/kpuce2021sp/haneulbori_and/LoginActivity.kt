package kr.ac.kpu.kpuce2021sp.haneulbori_and

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val signInBtn=findViewById<Button>(R.id.signInBtn)
        signInBtn.setOnClickListener(
            var intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        )
    }
}