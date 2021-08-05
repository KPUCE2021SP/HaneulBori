package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signInBtn.setOnClickListener {

            if(idEditText.text.toString()=="manager")
            {
                var intent= Intent(applicationContext,ManagerActivity::class.java)
                startActivity(intent)
            }
            else if(idEditText.text.toString()=="user")
            {
                var intent= Intent(applicationContext,MainActivity::class.java)
                startActivity(intent)
            }



        }
    }
}