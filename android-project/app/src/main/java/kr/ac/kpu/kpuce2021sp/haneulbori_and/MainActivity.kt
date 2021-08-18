package kr.ac.kpu.kpuce2021sp.haneulbori_and


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TabActivity
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : TabActivity()
{

    val DB: FirebaseFirestore = Firebase.firestore
    val laundryCollectionRef = DB.collection("laundry")
    val userCollectionRef = DB.collection("User")
    val laundries = arrayListOf<String>()
    val machines = arrayListOf<String>()
    val books = arrayListOf<String>()
    var nowLaundry: String = ""
    val user = Firebase.auth.currentUser
    var depth: Int = 0
    var bookTime: String = ""
    var bookDate: String = ""


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val tabHost=this.tabHost

        val tabSpecReserve=tabHost.newTabSpec("예약하기").setIndicator("예약하기")
        tabSpecReserve.setContent(R.id.tabReserve)
        tabHost.addTab(tabSpecReserve)

        val tabSpecCancel=tabHost.newTabSpec("취소하기").setIndicator("취소하기")
        tabSpecCancel.setContent(R.id.tabCancel)
        tabHost.addTab(tabSpecCancel)

        tabHost.currentTab=0

        showLaundry()
        usersBookList()

        refreshButton.setOnClickListener {
            refreshLaundry()
        }
        refresh.setOnClickListener {
            refreshUserBookList()
        }
        setTimeButton.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                bookTime = SimpleDateFormat("HH:mm").format(cal.time)

            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        setDateButton.setOnClickListener {
            val cal = Calendar.getInstance()

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                bookDate = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAll() {
        depth = 0
        showLaundry()
        usersBookList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshLaundry() {
        depth = 0
        showLaundry()
    }

    fun refreshUserBookList() {
        usersBookList()
    }

    fun usersBookList() {
        books.clear()
        if (user != null) {
            userCollectionRef.document(user.uid).get()
                .addOnSuccessListener {
                    val temp = it["bookList"] as ArrayList<String>

                    if (temp.size != 0) {
                        for (doc in temp){
                            books.add(doc)
                        }

                        bookList.adapter = search_activity.MyCustomAdapter(this, books)
                        bookList.setOnItemClickListener { parent, view, position, id ->
                            val dlg = AlertDialog.Builder(this@MainActivity)
                            dlg.setIcon(R.mipmap.ic_launcher)
                            dlg.setTitle("예약 취소")
                            dlg.setMessage("다음 시간의 예약을 취소하시겠습니까?\n${books[position]}")
                            dlg.setPositiveButton("확인") { _, _ ->
                                books.removeAt(position)
                                userCollectionRef.document(user.uid).update("bookList", books)
                                refreshUserBookList()
                            }
                            dlg.setNegativeButton("취소", null)
                            dlg.show()
                        }
                    }
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBackPressed() {
        when(depth){
            0 -> startActivity(
                Intent(this, LoginActivity::class.java)
            )

            1 -> startActivity(
                Intent(this, LoginActivity::class.java)
            )

            2 -> {
                depth -= 2
                showLaundry()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateNow() : String{
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showLaundry(){
        depth += 1
        laundries.clear()
        laundryCollectionRef.get()
            .addOnSuccessListener {
                for (doc in it) {
                    laundries.add(doc.id.toString())
                }
                laundryList.adapter = search_activity.MyCustomAdapter(this, laundries)
                laundryList.setOnItemClickListener { parent, view, position, id ->
                    showMachine(parent, position)
                }
            }
            .addOnFailureListener {
                Log.w("MainActivity", "Error getting documents: ")
            }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun showMachine(parent: AdapterView<*>, position: Int){
        machines.clear()
        depth += 1
        nowLaundry = parent.getItemAtPosition(position) as String
        laundryCollectionRef.document(nowLaundry)
            .collection("machine").get()
            .addOnSuccessListener {
                for (doc in it){
                    machines.add(doc.id.toString())
                }
                laundryList.adapter = search_activity.MyCustomAdapter(this, machines)
                laundryList.setOnItemClickListener { pa, v, po, i ->

                    laundryCollectionRef.document(nowLaundry)
                        .collection("machine")
                        .document(machines[po]).get()
                        .addOnSuccessListener {
                            val dlg= AlertDialog.Builder(this@MainActivity)
                            dlg.setIcon(R.mipmap.ic_launcher)

                            if (!(it["state"] as Boolean)){
                                dlg.setTitle("예약 할 수 없습니다.")
                                dlg.setMessage("사유: ${it["reason"].toString()}")
                                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
                                dlg.setPositiveButton("확인", null)
                            } else {
                                val temp = it["book"] as ArrayList<*>
                                if (temp.size == 0){
                                    dlg.setTitle("예약 가능 시간")
                                    dlg.setMessage("예약 내역이 없습니다. 예약하시겠습니까?")
                                    dlg.setNegativeButton("취소",null)
                                    dlg.setPositiveButton("확인") { dialog, which ->
                                        if (user != null) {
                                            userCollectionRef.document(user.uid).get()
                                                .addOnSuccessListener {
                                                    if (it["bookList"] == null){
                                                        Toast.makeText(this, "faild to access database", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        val item = it["bookList"] as ArrayList<String>

                                                        item.add("$bookDate $bookTime")
                                                        DB.runTransaction {
                                                            userCollectionRef.document(user.uid).update("bookList", item)
                                                            laundryCollectionRef.document(nowLaundry).collection("machine")
                                                                .document(machines[po]).get()
                                                                .addOnSuccessListener {
                                                                    val bookList = it["book"] as ArrayList<String>
                                                                    bookList.add("$bookDate $bookTime")
                                                                    laundryCollectionRef.document(nowLaundry).collection("machine")
                                                                        .document(machines[po])
                                                                        .update("book", bookList)
                                                                }
                                                        }

                                                    }
                                                }
                                        }

                                        tv.text = "예약 ${it.id}"
                                    }
                                } else {
                                    var massage: String = ""

                                    for (time in temp){
                                        if (time == null){
                                            break
                                        } else {
                                            val ttime = time as String
                                            massage = massage + ttime + "\n"
                                        }
                                    }
                                    dlg.setTitle("예약 가능 시간")
                                    dlg.setMessage("해당 시간에 예약이 있습니다\n${massage}")
                                    dlg.setPositiveButton("확인", null)
                                }
                                Toast.makeText(this, it["reason"].toString(), Toast.LENGTH_SHORT).show()
                            }
                            dlg.show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }


}