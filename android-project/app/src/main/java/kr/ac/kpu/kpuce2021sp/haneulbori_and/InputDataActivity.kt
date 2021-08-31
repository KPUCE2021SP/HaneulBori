package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_input_data.*

class InputDataActivity : AppCompatActivity() {

    val DB: FirebaseFirestore = Firebase.firestore // 데이터베이스 레퍼런스
    val userCollectionRef = DB.collection("User") // 유저 레퍼런스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data)

        aceeptButotn.setOnClickListener {
            val data= hashMapOf(
                "Birthday" to BirthdayInput.text.toString(),
                "NickName" to nameInput.text.toString(),
                "Sex" to genderInput.text.toString(),
                "PhoneNumber" to phoneInput.text.toString(),
                "bookList" to ArrayList<String>(),
                "Email" to Firebase.auth.currentUser?.email,
                "UserType" to true
            )

            Firebase.auth.currentUser?.let {
                userCollectionRef.document(it.uid)
                    .set(data)
                    .addOnSuccessListener {
                        startActivity(
                            Intent(this, MainActivity::class.java)
                        )
                    }
            }
        }



    }
}
