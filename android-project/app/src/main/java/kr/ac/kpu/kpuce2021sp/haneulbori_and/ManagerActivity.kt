package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.app.Activity
import android.app.ActivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_manager.*

class ManagerActivity : AppCompatActivity()
{

    var db=FirebaseFirestore.getInstance()
    var itemList= arrayListOf<ListLayout>()
    var adapter=ListAdapter(itemList)

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)




    }
}