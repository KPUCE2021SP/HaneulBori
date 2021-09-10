package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_input_data.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class InputDataActivity : AppCompatActivity() {

    val DB: FirebaseFirestore = Firebase.firestore // 데이터베이스 레퍼런스
    val userCollectionRef = DB.collection("User") // 유저 레퍼런스

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_data)

        var yearSpinnerList = mutableListOf<String>("년 ▼")
        for (i in Calendar.getInstance().get(Calendar.YEAR) downTo 1900) {
            yearSpinnerList.add(i.toString())
        }
        var yearAdapter = ArrayAdapter<String>(this, R.layout.signup_spinner_text, yearSpinnerList)
        yearSpinnerInput.adapter = yearAdapter

        var monthSpinnerList = listOf("월 ▼", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        var monthAdapter = ArrayAdapter<String>(this, R.layout.signup_spinner_text, monthSpinnerList)
        monthSpinnerInput.adapter = monthAdapter

        var lastDate : Int
        var daySpinnerList = mutableListOf<String>("일 ▼")

        //생일 년도 spinner
        yearSpinnerInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (yearSpinnerInput.selectedItemPosition != 0) {
                    when (monthSpinnerInput.selectedItem.toString()) {
                        "1", "3", "5", "7", "8", "10", "12" -> lastDate = 31
                        "4", "6", "9", "11" -> lastDate = 30
                        //윤년일 때
                        "2" -> if ((yearSpinnerInput.selectedItem.toString().toInt() % 4 == 0 && yearSpinner.selectedItem.toString().toInt() % 100 != 0) || yearSpinner.selectedItem.toString().toInt() % 400 == 0) lastDate = 29 else lastDate = 28
                        else -> lastDate = 0

                    }
                    daySpinnerList.clear()
                    daySpinnerList.add("일 ▼")
                    for (i in 1..lastDate) {
                        daySpinnerList.add(i.toString())
                    }
                }
                var dayAdapter = ArrayAdapter<String>(this@InputDataActivity, R.layout.signup_spinner_text, daySpinnerList)
                daySpinnerInput.adapter = dayAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //생일 월,일 spinner
        monthSpinnerInput.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (yearSpinnerInput.selectedItemPosition != 0) {
                    when (monthSpinnerInput.selectedItem.toString()) {
                        "1", "3", "5", "7", "8", "10", "12" -> lastDate = 31
                        "4", "6", "9", "11" -> lastDate = 30
                        "2" -> if ((yearSpinnerInput.selectedItem.toString().toInt() % 4 == 0 && yearSpinnerInput.selectedItem.toString().toInt() % 100 != 0) || yearSpinner.selectedItem.toString().toInt() % 400 == 0) lastDate = 29 else lastDate = 28
                        else -> lastDate = 0

                    }
                    daySpinnerList.clear()
                    daySpinnerList.add("일 ▼")
                    for (i in 1..lastDate) {
                        daySpinnerList.add(i.toString())
                    }
                }
                var dayAdapter = ArrayAdapter<String>(this@InputDataActivity, R.layout.signup_spinner_text, daySpinnerList)
                daySpinnerInput.adapter = dayAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //전화번호 양식 확인
        phoneETInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var pattern : Pattern = Pattern.compile("010[0-9]{8}")
                var matcher : Matcher = pattern.matcher(phoneETInput.text.toString())
                if (matcher.find() && phoneETInput.text.length == 11) {
                    phoneCheckTextViewInput.visibility = android.view.View.INVISIBLE
                } else {
                    phoneCheckTextViewInput.visibility = android.view.View.VISIBLE
                }
            }

        })

        signupBtnInput.setOnClickListener {
            var gender = "M"
            when(radioGroupInput.checkedRadioButtonId){
                R.id.manBtnInput -> gender = "M"
                R.id.womanBtnInput -> gender = "W"
            }

            var mon = ""
            var d = ""

            mon = if (monthSpinnerInput.selectedItem.toString().toInt() < 10) {
                "0${monthSpinnerInput.selectedItem}"
            } else {
                "${monthSpinnerInput.selectedItem}"
            }

            d = if (daySpinnerInput.selectedItem.toString().toInt() < 10){
                "0${daySpinnerInput.selectedItem}"
            } else {
                "${daySpinnerInput.selectedItem}"
            }
            val birthday = "${yearSpinnerInput.selectedItem}" + mon + d


            val data = hashMapOf(
                "Birthday" to birthday,
                "Name" to nameETInput.text.toString(),
                "Sex" to gender,
                "PhoneNumber" to phoneETInput.text.toString(),
                "bookList" to ArrayList<String>(),
                "Email" to IdText.text.toString(),
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
