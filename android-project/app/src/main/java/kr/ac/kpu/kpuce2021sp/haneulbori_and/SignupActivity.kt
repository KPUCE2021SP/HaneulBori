package kr.ac.kpu.kpuce2021sp.haneulbori_and

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

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
    }
}