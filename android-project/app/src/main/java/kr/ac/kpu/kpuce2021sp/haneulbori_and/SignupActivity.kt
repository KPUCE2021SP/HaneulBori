package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
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

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //비밀번호 = 비밀번호 확인
        pwCheckET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (pwET.text.toString() != pwCheckET.text.toString()) {
                    pwCheckTextView.visibility = android.view.View.VISIBLE
                } else {
                    pwCheckTextView.visibility = android.view.View.INVISIBLE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })

        var yearSpinnerList = mutableListOf<String>("년 ▼")
        for (i in Calendar.getInstance().get(Calendar.YEAR) downTo 1900) {
            yearSpinnerList.add(i.toString())
        }
        var yearAdapter = ArrayAdapter<String>(this, R.layout.signup_spinner_text, yearSpinnerList)
        yearSpinner.adapter = yearAdapter

        var monthSpinnerList = listOf("월 ▼", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12")
        var monthAdapter = ArrayAdapter<String>(this, R.layout.signup_spinner_text, monthSpinnerList)
        monthSpinner.adapter = monthAdapter

        var lastDate : Int
        var daySpinnerList = mutableListOf<String>("일 ▼")

        //생일 년도 spinner
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (yearSpinner.selectedItemPosition != 0) {
                    when (monthSpinner.selectedItem.toString()) {
                        "1", "3", "5", "7", "8", "10", "12" -> lastDate = 31
                        "4", "6", "9", "11" -> lastDate = 30
                        //윤년일 때
                        "2" -> if ((yearSpinner.selectedItem.toString().toInt() % 4 == 0 && yearSpinner.selectedItem.toString().toInt() % 100 != 0) || yearSpinner.selectedItem.toString().toInt() % 400 == 0) lastDate = 29 else lastDate = 28
                        else -> lastDate = 0

                    }
                    daySpinnerList.clear()
                    daySpinnerList.add("일 ▼")
                    for (i in 1..lastDate) {
                        daySpinnerList.add(i.toString())
                    }
                }
                var dayAdapter = ArrayAdapter<String>(this@SignupActivity, R.layout.signup_spinner_text, daySpinnerList)
                daySpinner.adapter = dayAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //생일 월,일 spinner
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (yearSpinner.selectedItemPosition != 0) {
                    when (monthSpinner.selectedItem.toString()) {
                        "1", "3", "5", "7", "8", "10", "12" -> lastDate = 31
                        "4", "6", "9", "11" -> lastDate = 30
                        "2" -> if ((yearSpinner.selectedItem.toString().toInt() % 4 == 0 && yearSpinner.selectedItem.toString().toInt() % 100 != 0) || yearSpinner.selectedItem.toString().toInt() % 400 == 0) lastDate = 29 else lastDate = 28
                        else -> lastDate = 0

                    }
                    daySpinnerList.clear()
                    daySpinnerList.add("일 ▼")
                    for (i in 1..lastDate) {
                        daySpinnerList.add(i.toString())
                    }
                }
                var dayAdapter = ArrayAdapter<String>(this@SignupActivity, R.layout.signup_spinner_text, daySpinnerList)
                daySpinner.adapter = dayAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        //전화번호 양식 확인
        phoneET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var pattern : Pattern = Pattern.compile("010[0-9]{8}")
                var matcher : Matcher = pattern.matcher(phoneET.text.toString())
                if (matcher.find() && phoneET.text.length == 11) {
                    phoneCheckTextView.visibility = android.view.View.INVISIBLE
                } else {
                    phoneCheckTextView.visibility = android.view.View.VISIBLE
                }
            }

        })

        //회원가입 버튼
        signupBtn.setOnClickListener {
            if (!signUpButtonEnable()) {
                var dlg = AlertDialog.Builder(this@SignupActivity)
                dlg.setTitle("가입 실패")
                dlg.setMessage("작성이 완료되지 않았습니다.")
                dlg.setIcon(R.mipmap.ic_launcher)
                dlg.setPositiveButton("확인", null)
                dlg.show()
            } else {
                val DB: FirebaseFirestore = Firebase.firestore // 데이터베이스 레퍼런스
                val userCollectionRef = DB.collection("User") // 유저 레퍼런스

                var gender = "M"
                when(radioGroup.checkedRadioButtonId){
                    R.id.manBtn -> gender = "M"
                    R.id.womanBtn -> gender = "W"
                }

                var mon = ""
                var d = ""

                if (monthSpinner.selectedItem.toString().toInt() < 10) {
                    mon = "0${monthSpinner.selectedItem}"
                } else {
                    mon = "${monthSpinner.selectedItem}"
                }

                if (daySpinner.selectedItem.toString().toInt() < 10){
                    d = "0${daySpinner.selectedItem}"
                } else {
                    d = "${daySpinner.selectedItem}"
                }
                val birthday = "${yearSpinner.selectedItem}" + mon + d


                val data = hashMapOf(
                    "Birthday" to birthday,
                    "Name" to nameET.text.toString(),
                    "Sex" to gender,
                    "PhoneNumber" to phoneET.text.toString(),
                    "bookList" to ArrayList<String>(),
                    "Email" to idET.text.toString(),
                    "UserType" to true
                )




                Firebase.auth.createUserWithEmailAndPassword(idET.text.toString(), pwET.text.toString())
                    .addOnFailureListener {
                        Toast.makeText(this, "회원가입에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnCompleteListener {
                        Firebase.auth.signInWithEmailAndPassword(idET.text.toString(), pwET.text.toString())
                            .addOnSuccessListener {
                                userCollectionRef.document(Firebase.auth.currentUser?.uid.toString()).set(data)
                                startActivity(
                                    Intent(this, MainActivity::class.java)
                                )
                            }
                    }



            }
        }

    }

    fun signUpButtonEnable() : Boolean {
        if (idET.text.isEmpty() || idCheckTextView.visibility == View.VISIBLE ||
            pwET.text.isEmpty() || pwCheckET.text.isEmpty() || pwCheckTextView.visibility == View.VISIBLE ||
            nameET.text.isEmpty() || (!manBtn.isChecked && !womanBtn.isChecked) ||
            yearSpinner.selectedItemPosition == 0 || monthSpinner.selectedItemPosition == 0 || daySpinner.selectedItemPosition == 0 ||
            phoneCheckTextView.visibility == View.VISIBLE ) {
            return false
        }
        return true
    }
}