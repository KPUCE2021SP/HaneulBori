package kr.ac.kpu.kpuce2021sp.haneulbori_and


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.TabActivity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.laundry_iteam.*
import kotlinx.android.synthetic.main.report_view.*
import kotlinx.android.synthetic.main.report_view.view.*
import kotlinx.android.synthetic.main.time_interver_layout.view.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : TabActivity()
{

    val DB: FirebaseFirestore = Firebase.firestore // 데이터베이스 레퍼런스
    val laundryCollectionRef = DB.collection("laundry") // 세탁소 레퍼런스
    val userCollectionRef = DB.collection("User") // 유저 레퍼런스
    val laundries = arrayListOf<String>() // 세탁소 임시 저장 문자열 (리스트용)
    val machines = arrayListOf<String>() // 세탁기 임시 저장 문자열 (리스트용)
    val iconImages = arrayListOf<Drawable>() // 임시 저장 이미지 (리스트용)
    val books = arrayListOf<String>() // 사용자 예약 내역 임시 저장 문자열 (리스트용)
    val tempBooks = arrayListOf<String>() // 사용자 예약 내역 임시 저장 문자열 (리스트용)
    var nowLaundry: String = "" // 현재 세탁소 저장용
    val user = Firebase.auth.currentUser // 사용자 정보
    var depth: Int = 0 // 리스트 깊이
    var bookInterver: String = "" // 세탁기 사용 시간
    var startTime: String = "" // 세탁기 사용 시작 시간
    var startDate: String = "" // 세탁기 사용 시작 일자
    var endTime: String = "" // 세탁기 사용 종료 시간
    var endDate: String = "" // 세탁기 사용 종료 일자



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 탭 호스트 및 설정
        val tabHost=this.tabHost

        val tabSpecReserve=tabHost.newTabSpec("예약하기").setIndicator("예약하기")
        tabSpecReserve.setContent(R.id.tabReserve)
        tabHost.addTab(tabSpecReserve)

        val tabSpecCancel=tabHost.newTabSpec("취소하기").setIndicator("취소하기")
        tabSpecCancel.setContent(R.id.tabCancel)
        tabHost.addTab(tabSpecCancel)

        tabHost.currentTab=0

        // 리스트 시작
        showLaundry()
        usersBookList()

        // 각 버튼 리스너

        // 새로고침 (세탁기 및 세탁소)
        refreshButton.setOnClickListener {
            refreshLaundry()
        }




        // 새로고침 버튼 (유저 예약 정보)
        refresh.setOnClickListener {
            refreshUserBookList()
        }

        // 시간 설정 시트 버튼
        setTimeButton.setOnClickListener{

            val bottomSheet = BottomSheetDialog(this)

            bottomSheet.setContentView(R.layout.activity_book)
            val cal = bottomSheet.bookDatePicker
            cal.minDate = System.currentTimeMillis()
            cal.setOnDateChangeListener { view, year, month, dayOfMonth ->

                val yearText = "$year"

                val monthText = if (month <= 8){
                    "0${month + 1}"
                } else {
                    "${month + 1}"
                }

                val dayText = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    "$dayOfMonth"
                }

                startDate = "$yearText-$monthText-$dayText"
            }
            bottomSheet.acceptButton.setOnClickListener {

                val time = bottomSheet.bookTimePicker

                val current = Calendar.getInstance()

                val timeText = if (time.hour < 10) {
                    "0${time.hour}"
                } else {
                    "${time.hour}"
                }

                val minuteText = if (time.minute < 10) {
                    "0${time.minute}"
                } else {
                    "${time.minute}"
                }

                startTime = "$timeText:$minuteText"
                if (false){
                    val dlg = AlertDialog.Builder(this)
                    dlg.setTitle("시간 오류")
                    dlg.setMessage("이미 지난시간을 선택하셨습니다.")
                    dlg.setPositiveButton("확인", null)
                    dlg.show()
                } else {

                    if (startDate == ""){
                        startDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                    }
                    Toast.makeText(this, "$startDate ${startTime}에 ${bookInterver}분 사용 예약 되었습니다.", Toast.LENGTH_SHORT). show()
                    bottomSheet.dismiss()
                }
            }

            bottomSheet.timeSet50Button.setOnClickListener {
                bookInterver = "50"
            }

            bottomSheet.timeSet80Button.setOnClickListener {
                bookInterver = "80"
            }

            bottomSheet.timeSetFreeButton.setOnClickListener {
                val dlg = AlertDialog.Builder(this)
                val dlgView = layoutInflater.inflate(R.layout.time_interver_layout, null)
                val dlgText = dlgView.interverInput

                dlg.setView(dlgView)

                dlg.setPositiveButton("확인") { dialog, _ ->
                    if (dlgText.text == null){
                        Toast.makeText(this, "올바른 시간을 입력해 주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        bookInterver = dlgText.text.toString()
                    }
                }
                dlg.setNegativeButton("취소", null)
                dlg.show()
            }

            bottomSheet.show()
        }

        reportButton.setOnClickListener {
            val templist = mutableListOf<String>()

            val dlg = AlertDialog.Builder(this)
            val dlgView = layoutInflater.inflate(R.layout.report_view, null)

            var ts = ""

            templist.add("세탁소 ▼")

            laundryCollectionRef
                .document(nowLaundry)
                .collection("machine")
                .get()
                .addOnSuccessListener {
                    for (doc in it){
                        templist.add(doc.id)
                    }
                }

            dlgView.nowLaundry.text = nowLaundry

            val machineSpinnerList = templist
            val machineAdapter = ArrayAdapter<String>(this, R.layout.signup_spinner_text, machineSpinnerList)
            dlgView.machineSpinner.adapter = machineAdapter

            dlg.setPositiveButton("신고하기") { _, _ ->
                if ((ts == "") || (dlgView.reportReasonText.text.toString() == "")){
                    val refuseDlg = AlertDialog.Builder(this)
                    refuseDlg.setTitle("오류")
                    refuseDlg.setMessage("세탁기 지정이 잘못되었습니다.")
                    refuseDlg.setPositiveButton("확인", null)
                    refuseDlg.show()
                } else {
                    if (user != null) {
                        val itemMap = hashMapOf(
                            "user" to user.uid,
                            "reason" to dlgView.reportReasonText.text.toString(),
                            "where" to nowLaundry,
                            "which" to ts
                        )
                        DB.collection("Report").document("${user.uid}_${nowLaundry}_$ts")
                            .set(itemMap)
                    }
                }
            }

            dlg.setNegativeButton("취소",null)

            dlgView.machineSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
                AdapterView.OnItemClickListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ){
                    ts = templist[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                }
            }



                dlg.setView(dlgView)
            dlg.setTitle("고장신고")

            dlg.show()

        }




    }

    // 전체 새로고침
    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAll() {
        depth = 0
        showLaundry()
        usersBookList()
    }

    // 세탁소 및 세탁기 새로고침 함수
    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshLaundry() {

        if (depth == 1){
            depth = 0
            Toast.makeText(this, "refresh laundry", Toast.LENGTH_SHORT).show()
            showLaundry()
        } else if (depth == 2){
            depth = 1
            Toast.makeText(this, "refresh machine", Toast.LENGTH_SHORT).show()
            showMachine()
        }
    }

    // 유저 리스트 새로고침 함수
    fun refreshUserBookList() {
        usersBookList()
    }

    // 사용자 예약 정보 출력
    fun usersBookList() {

        // 사용자 예약 내역 출력
        if (user != null) {
            books.clear()
            tempBooks.clear()
            userCollectionRef.document(user.uid).get()
                .addOnSuccessListener {
                    val temp = it["bookList"] as ArrayList<String>
                    if (temp.size != 0) {
                        for (doc in temp) {
                            books.add(doc)
                            tempBooks.add(BookStringToword(doc))
                        }
                        bookList.adapter = search_activity.MyCustomAdapter(this, tempBooks)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "fail to access", Toast.LENGTH_SHORT).show()
                }

            // 각 리스트 아이템의 리스너
            bookList.setOnItemClickListener { parent, view, position, id ->
                val dlg = AlertDialog.Builder(this@MainActivity)
                val bookData: List<String> = books[position].split("//")
                dlg.setIcon(R.mipmap.ic_launcher)
                dlg.setTitle("예약 취소")
                dlg.setMessage("다음 시간의 예약을 취소하시겠습니까?\n${bookData[0]}\n${bookData[1]}\n${bookData[2]}\n${bookData[3]}")
                dlg.setPositiveButton("확인") { _, _ ->
                    var item: ArrayList<String>

                    val userSnapshot = userCollectionRef.document(user.uid)
                    val laundrySnapshot = laundryCollectionRef.document("12floor").collection("machine")
                        .document(bookData[3])

                    books.removeAt(position)
                    tempBooks.removeAt(position)
                    laundrySnapshot.get()
                        .addOnSuccessListener {
                            item = it["book"] as ArrayList<String>

                            for (doc in item) {
                                val tempDoc = doc.split("//")
                                if ((tempDoc[0] + "//" + tempDoc[1]) == (bookData[0] + "//" + bookData[1])) {
                                    item.remove(doc)
                                    Toast.makeText(this, item.size.toString(), Toast.LENGTH_SHORT).show()
                                    // 실제 DB에 반영되는 트랜젝션
                                    DB.runTransaction { Transaction ->
                                        Transaction.update(userSnapshot, "bookList", books)
                                        Transaction.update(laundrySnapshot, "book", item)
                                    }
                                        .addOnSuccessListener {
                                            // 성공시 새로고침
                                            refreshUserBookList()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(this, "failed to cancel.", Toast.LENGTH_SHORT).show()
                                        }
                                    break
                                }
                            }
                        }





                }
                dlg.setNegativeButton("취소", null)
                dlg.show()
            }


        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    // 뒤로가기 버튼 눌렀을 시
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

    // 세탁소 출력 함수
    @RequiresApi(Build.VERSION_CODES.O)
    fun showLaundry(){
        // 깊이 측적용 변수
        depth += 1

        // 임시 데이터 삭제
        laundries.clear()
        iconImages.clear()
        nowLaundry = ""
        laundryText.text = "세탁소 선택"
        refreshButton.visibility = android.view.View.GONE
        reportButton.visibility = android.view.View.GONE
        setTimeButton.visibility = View.GONE

        // 레퍼런스에서 데이터 가져와 리스트에 출력
        laundryCollectionRef.get()
            .addOnSuccessListener {
                for (doc in it) {
                    laundries.add(doc.id.toString())
                    iconImages.add(ContextCompat.getDrawable(this, R.drawable.laundry_image)!!)
                }
                laundryList.adapter = search_activity.MyCustomAdapter(this, laundries, iconImages)

                // 아이템 리스너
                laundryList.setOnItemClickListener { parent, view, position, id ->
                    // 세탁기 리스트 출력
                    nowLaundry = parent.getItemAtPosition(position) as String
                    showMachine()
                    laundryText.text = nowLaundry
                    refreshButton.visibility = View.VISIBLE
                    reportButton.visibility = View.VISIBLE
                    setTimeButton.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener {
                Log.w("MainActivity", "Error getting documents: ")
            }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun showMachine(){
        // 깊이 및 리스트 초기화
        machines.clear()
        iconImages.clear()
        depth += 1

        // 세탁기 DB 접근
        laundryCollectionRef.document(nowLaundry)
            .collection("machine").get()
            .addOnSuccessListener {
                for (doc in it){
                    if (doc.id.slice(IntRange(0, 0)) == "d") {
                        iconImages.add(ContextCompat.getDrawable(this, R.drawable.drying_image)!!)
                    } else {
                        iconImages.add(ContextCompat.getDrawable(this, R.drawable.washing_image)!!)
                    }
                    machines.add(doc.id)
                }

                laundryList.adapter = search_activity.MyCustomAdapter(this, machines, iconImages)
                laundryList.setOnItemClickListener { pa, v, po, i ->

                    // 세탁기 선택 후 예약 여부 확인 및 예약
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

                                // 해당 시간에 예약이 가능한지 확인
                                if (temp.size != 0) {
                                    for (tempTime in temp) {
                                        val tempTotal = tempTime.split("//")

                                        var tempStart = tempTotal[0]
                                        var tempEnd = tempTotal[1]

                                        val tempStartDateTime = tempTotal[0].split(" ")
                                        val tempStartDate = tempStartDateTime[0]
                                        val tempStartTime = tempStartDateTime[1]

                                        tempStart = "$tempStartDate+$tempStartTime"

                                        val tempEndDateTime = tempTotal[1].split(" ")
                                        val tempEndDate = tempEndDateTime[0]
                                        val tempEndTime = tempEndDateTime[1]

                                        tempEnd = "$tempEndDate+$tempEndTime"
                                        Log.d("temp", tempEnd)

                                        val simpleFormat =  DateTimeFormatter.ISO_DATE

                                        val tempsDate = LocalDateTime.parse(tempStart, DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm"))
                                        val tempeDate = LocalDateTime.parse(tempEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm"))

                                        val sDate = LocalDateTime.parse("$startDate+$startTime", DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm"))
                                        val eDate = LocalDateTime.parse("$endDate+$endTime", DateTimeFormatter.ofPattern("yyyy-MM-dd+HH:mm"))
                                        Log.d("DateCheck", "\ntempsDate: $tempsDate\n" +
                                                "tempeDate: $tempeDate\n" +
                                                "sDate: $sDate\n" +
                                                "eDate: $eDate\n")

                                        canBook = if (
                                            ((tempsDate < eDate) && (eDate < tempeDate)) ||
                                            ((tempsDate < sDate) && (sDate < tempeDate))
                                        ){
                                            false
                                        } else !((sDate < tempsDate) && (tempeDate < eDate))

                                    }
                                } else {
                                    canBook = true
                                }

                                // 예약이 가능한 경우 예약 진행
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
                                                        val phoneNumber = it["PhoneNumber"]
                                                        item.add("$startDate $startTime//$endDate $endTime//$phoneNumber//${machines[po]}")
                                                        DB.runTransaction {
                                                            userCollectionRef.document(user.getUid()).update("bookList", item)
                                                            laundryCollectionRef.document(nowLaundry).collection("machine")
                                                                .document(machines[po]).get()
                                                                .addOnSuccessListener {
                                                                    val bookList = it["book"] as ArrayList<String>
                                                                    bookList.add("$startDate $startTime//$endDate $endTime//$phoneNumber")
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

                                    }
                                } else {
                                    // 예약이 불가능한 경우 메세지 출력
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
                                    dlg.setMessage("해당 시간에 예약이 있습니다\n${BookStirngToword2(massage)}")
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

    // 시간 데이터를 포멧에 맞게 변경 및 더하기
    fun addTimeDate(time: String, date: String, interver: String) {

        endDate = ""
        endTime = ""

        val ttime = time.split(":").toMutableList()
        val tdate = date.split("-").toMutableList()
        var tinterver = interver.toInt() + ttime[1].toInt()

        ttime[1] = tinterver.toString()

        while (tinterver >= 60) {
            ttime[1] = (ttime[1].toInt() - 60).toString()
            ttime[0] = (ttime[0].toInt() + 1).toString()
            tinterver -= 60
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
        if (ttime[0].toInt() < 10 && (ttime[0].length < 2)){
            ttime[0] = "0" + ttime[0]
        }
        if (ttime[1].toInt() < 10 && (ttime[1].length < 2)){
            ttime[1] = "0" + ttime[1]
        }

        endDate = tdate[0] + "-" + tdate[1] + "-" + tdate[2]
        endTime = ttime[0] + ":" + ttime[1]

    }

    private fun BookStringToword(inputData : String) : String {

        val temp = inputData.split("//")

        return "시작: ${temp[0]}\n만료: ${temp[1]}\n세탁소: ${temp[2]}\n기기: ${temp[3]}"

    }

    private fun BookStirngToword2(inputData : String) : String {
        val temp = inputData.split("//")

        return "시작: ${temp[0]}\n만료: ${temp[1]}"
    }

}