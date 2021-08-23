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
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.time_interver_layout.view.*


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
    var bookInterver: String = ""
    var startTime: String = ""
    var startDate: String = ""
    var endTime: String = ""
    var endDate: String = ""
    var nowMachine: String = ""


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

        setInterverButton.setOnClickListener {

            val dlg = AlertDialog.Builder(this)
            val dlgView = layoutInflater.inflate(R.layout.time_interver_layout, null)
            val dlgText = dlgView.interverInput

            dlg.setView(dlgView)

            dlg.setPositiveButton("확인") { dialog, _ ->
                bookInterver = dlgText.text.toString()
            }
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }

        refresh.setOnClickListener {
            refreshUserBookList()
        }

        setTimeButton.setOnClickListener {
            val cal = Calendar.getInstance()

            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                startTime = SimpleDateFormat("HH:mm").format(cal.time)

            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        setDateButton.setOnClickListener {
            val cal = Calendar.getInstance()

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                startDate = SimpleDateFormat("yyyy-MM-dd").format(cal.time)
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

        if (depth == 1){
            depth = 0
            showLaundry()
        } else if (depth == 2){
            depth = 1
            showMachine()
        }
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
                                DB.runTransaction {
                                    val bookData: List<String> = books[position].split("//")

                                    books.removeAt(position)
                                    userCollectionRef.document(user.uid).update("bookList", books)
                                    refreshUserBookList()
                                    // Toast.makeText(this, bookData[2], Toast.LENGTH_SHORT).show()
                                    laundryCollectionRef.document(bookData[1]).collection("machine")
                                        .document(bookData[2]).get()
                                        .addOnSuccessListener {
                                            val item = it["book"] as ArrayList<String>
                                            for (doc in item) {
                                                if (doc == bookData[0]) {
                                                    item.remove(doc)
                                                    break
                                                }
                                            }
                                            laundryCollectionRef.document(bookData[1])
                                                .collection("machine")
                                                .document(bookData[2]).update("book", item)
                                        }
                                }
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
    fun showLaundry(){
        depth += 1
        laundries.clear()
        nowLaundry = ""
        laundryCollectionRef.get()
            .addOnSuccessListener {
                for (doc in it) {
                    laundries.add(doc.id.toString())
                }
                laundryList.adapter = search_activity.MyCustomAdapter(this, laundries)
                laundryList.setOnItemClickListener { parent, view, position, id ->
                    nowLaundry = parent.getItemAtPosition(position) as String
                    showMachine()
                }
            }
            .addOnFailureListener {
                Log.w("MainActivity", "Error getting documents: ")
            }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun showMachine(){
        machines.clear()
        depth += 1
        laundryCollectionRef.document(nowLaundry)
            .collection("machine").get()
            .addOnSuccessListener {
                for (doc in it){
                    machines.add(doc.id)
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
                            } else if (startDate == "" || startTime == "" || bookInterver == ""){
                                dlg.setTitle("예약 할 수 없습니다.")
                                dlg.setMessage("예약 시간을 정확히 입력해 주세요")
                                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
                                dlg.setPositiveButton("확인", null)
                            } else {
                                addTimeDate(startTime, startDate, bookInterver)
                                val temp = it["book"] as ArrayList<String>
                                var canBook = true

                                if (!(temp.size == 0)) {
                                    for (tempTime in temp) {
                                        val tempTotal = tempTime.split("//")

                                        val tempStart = tempTotal[0]
                                        val tempEnd = tempTotal[1]

                                        val tempStartDateTime = tempTotal[0].split(" ")
                                        val tempStartDate = tempStartDateTime[0]
                                        val tempStartTime = tempStartDateTime[1]

                                        val tempEndDateTime = tempTotal[1].split(" ")
                                        val tempEndDate = tempEndDateTime[0]
                                        val tempEndTime = tempEndDateTime[1]

                                        canBook =
                                            !(tempStart <= "$startDate $startTime" && "$startDate $startTime" <= tempEnd)
                                    }
                                } else {
                                    canBook = true
                                }

                                if (canBook){
                                    dlg.setTitle("예약")
                                    dlg.setMessage("예약 내역이 없습니다. 예약하시겠습니까?")
                                    dlg.setNegativeButton("취소",null)
                                    dlg.setPositiveButton("확인") { dialog, which ->
                                        if (user != null) {
                                            userCollectionRef.document(user.uid).get()
                                                .addOnSuccessListener {
                                                    if (it["bookList"] == null){
                                                        Toast.makeText(this, "failed to access database", Toast.LENGTH_SHORT).show()
                                                    } else {
                                                        val item = it["bookList"] as ArrayList<String>

                                                        item.add("$startDate $startTime//$endDate $endTime//$nowLaundry//${machines[po]}")
                                                        DB.runTransaction {
                                                            userCollectionRef.document(user.uid).update("bookList", item)
                                                            laundryCollectionRef.document(nowLaundry).collection("machine")
                                                                .document(machines[po]).get()
                                                                .addOnSuccessListener {
                                                                    val bookList = it["book"] as ArrayList<String>
                                                                    bookList.add("$startDate $startTime//$endDate $endTime")
                                                                    laundryCollectionRef.document(nowLaundry).collection("machine")
                                                                        .document(machines[po])
                                                                        .update("book", bookList)
                                                                }
                                                            refreshUserBookList()
                                                        }

                                                    }
                                                }
                                        } else {
                                            Toast.makeText(this, "Login failed. try again", Toast.LENGTH_SHORT).show()
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

    fun addTimeDate(time: String, date: String, interver: String) {

        endDate = ""
        endTime = ""

        val ttime = time.split(":").toMutableList()
        val tdate = date.split("-").toMutableList()
        val tinterver = interver.toInt() + ttime[1].toInt()

        ttime[1] = tinterver.toString()

        while (tinterver > 60) {
            ttime[1] = (ttime[1].toInt() - 60).toString()
            ttime[0] = (ttime[0].toInt() + 1).toString()
        }

        if (ttime[0].toInt() > 24){
            tdate[2] = (tdate[2].toInt() + 1).toString()
        }

        if (tdate[2].toInt() > 28){
            when (tdate[1].toInt()){
                1, 3, 5, 7, 8, 10, 12 -> {
                    if (tdate[2].toInt() > 31){
                        tdate[1] = (tdate[1].toInt() + 1).toString()
                        if (tdate[1] == "13"){
                            tdate[1] = "1"
                            tdate[0] = (tdate[0].toInt() + 1).toString()
                        }
                        tdate[2] = "1"
                    }
                }

                4, 6, 9, 11 -> {
                    if (tdate[2].toInt() > 30){
                        tdate[1] = (tdate[1].toInt() + 1).toString()
                        tdate[2] = "1"
                    }
                }

                2 -> {
                    val temp = tdate[0].toInt()
                    var yun = false
                    var tempdate = 28

                    if (temp % 4 == 0){
                        yun = true
                        if (temp % 100 == 0){
                            yun = false
                            if (temp % 400 == 0){
                                yun = true
                            }
                        }
                    } else {
                        yun = false
                    }

                    if (yun){
                        tempdate = 29
                    }

                    if (tdate[2].toInt() > tempdate){
                        tdate[1] = (tdate[1].toInt() + 1).toString()
                        tdate[2] = "1"
                    }
                }
            }
        }
        if ((tdate[2].toInt() < 10) && (tdate[2].length < 2)){
            tdate[2] = "0" + tdate[2]
        }
        if (tdate[1].toInt() < 10 && (tdate[1].length < 2)){
            tdate[1] = "0" + tdate[1]
        }
        if (ttime[0].toInt() < 10 && (ttime[1].length < 2)){
            ttime[0] = "0" + ttime[0]
        }
        if (ttime[1].toInt() < 10 && (ttime[1].length < 2)){
            ttime[1] = "0" + ttime[1]
        }

        endDate = tdate[0] + "-" + tdate[1] + "-" + tdate[2]
        endTime = ttime[0] + ":" + ttime[1]

    }


}