package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_manager.*
import java.time.LocalDate
import kotlin.collections.hashMapOf as hashMapOf

class ManagerActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    val itemList = arrayListOf<ListLayout>()
    var machineList = arrayListOf<MachineList>()
    var statusList = arrayListOf<StatusList>()
    val adapter = ListAdapter(itemList)

    @RequiresApi(Build.VERSION_CODES.O)
    var date = LocalDate.now()
    var errorList = arrayListOf<ErrorList>()
    var realList = arrayListOf<String>()
    var todayUser = arrayListOf<String>()
    var washerCnt = 0
    var drierCnt = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)



        db.collection("laundry").document("12floor").collection("machine")
            .get()
            .addOnSuccessListener { result ->
                realList.clear()
                for (document in result) {
                    val item = MachineList(document["num"] as Long, document["type"] as String)
                    var tmp = ""
                    if (item.type == "washer") {
                        tmp = "w" + item.num.toString()
                        washerCnt++
                    } else if (item.type == "drier") {
                        tmp = "d" + item.num.toString()
                        drierCnt++
                    }
                    realList.add(tmp)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext, "Fail", Toast.LENGTH_SHORT).show()
            }

        //?????? ????????? ??????
        btnToday.setOnClickListener {
            var intent= Intent(applicationContext,ShowUser::class.java)
            intent.putExtra("key","a")
            startActivity(intent)
        }

        // ????????? ????????? ??????
        btnCheckUser.setOnClickListener {
            var intent= Intent(applicationContext,ShowUser::class.java)
            intent.putExtra("key","b")
            startActivity(intent)
        }

        //?????? ??????
        btnAddMachine.setOnClickListener {
            var choiceMachine = arrayOf<String>("?????????", "?????????")
            var dlg = AlertDialog.Builder(this@ManagerActivity)
            var choiceItem = "?????????"
            dlg.setTitle("????????? ????????? ???????????????")
            dlg.setSingleChoiceItems(choiceMachine, 0) { dialog, which ->
                choiceItem = choiceMachine[which]
            }
            dlg.setPositiveButton("??????") { dialog, which ->
                if (choiceItem.equals("?????????"))
                {
                    val data = hashMapOf(
                        "book" to ArrayList<String>(), "reason" to "normal", "state" to true, "type" to "washer", "num" to (++washerCnt))
                    var tmp = "w" + (washerCnt).toString()
                    db.collection("laundry").document("12floor").collection("machine").document(tmp)
                        .set(data)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext, "????????? ?????? ??????", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "????????? ?????? ??????", Toast.LENGTH_SHORT).show()
                        }
                }
                else if (choiceItem.equals("?????????"))
                {
                    val data = hashMapOf(
                        "book" to ArrayList<String>(), "reason" to "normal", "state" to true, "type" to "drier", "num" to (++drierCnt))
                    var tmp = "d" + (drierCnt).toString()
                    db.collection("laundry").document("12floor").collection("machine").document(tmp)
                        .set(data)
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext, "????????? ?????? ??????", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "????????? ?????? ??????", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            dlg.setNegativeButton("??????") { dialog, which ->
                Toast.makeText(applicationContext, "??????????????????", Toast.LENGTH_SHORT).show()
            }
            dlg.show()
        }

        //?????? ??????
        btnDeleteMachine.setOnClickListener {
            db.collection("laundry").document("12floor").collection("machine")
                .get()
                .addOnSuccessListener { result ->
                    machineList.clear()
                    for (document in result) {
                        val item = MachineList(document["num"] as Long, document["type"] as String)
                        machineList.add(item)
                    }
                    var selectMachine = ""
                    var deleteMachine = arrayOf<String>(
                        "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -",
                        "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -")
                    var washerMachine = arrayListOf<MachineList>()
                    var drierMachine = arrayListOf<MachineList>()
                    var dlg = AlertDialog.Builder(this@ManagerActivity)
                    dlg.setTitle("????????? ????????? ???????????????")

                    for (i in machineList.indices)
                    {
                        if (machineList[i].type == "washer")
                        {
                            washerMachine.add(machineList[i])
                            deleteMachine[i] = machineList[i].num.toString() + "??? ?????????"
                        } else
                        {
                            drierMachine.add(machineList[i])
                            deleteMachine[i] = machineList[i].num.toString() + "??? ?????????"
                        }
                    }
                    dlg.setSingleChoiceItems(deleteMachine, 0) { dialog, which ->
                        selectMachine = deleteMachine[which]
                    }
                    dlg.setPositiveButton("??????") { dialog, which ->
                        var tmp = ""
                        if (selectMachine.contains("?????????")) {
                            tmp = "w" + selectMachine[0].toString()
                            washerCnt--
                        } else if (selectMachine.contains("?????????")) {
                            tmp = "d" + selectMachine[0].toString()
                            drierCnt--
                        }
                        db.collection("laundry").document("12floor").collection("machine")
                            .document("" + tmp)
                            .delete()
                        Toast.makeText(
                            applicationContext,
                            selectMachine + "??? ??????????????????",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    dlg.setNegativeButton("??????") { dialog, which ->
                        Toast.makeText(applicationContext, "??????????????????", Toast.LENGTH_SHORT).show()
                    }
                    dlg.show()
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, "Fail", Toast.LENGTH_SHORT).show()
                }
        }

        //?????? ?????? ??????
        btnSearchMachine.setOnClickListener {
            db.collection("laundry").document("12floor").collection("machine")
                .get()
                .addOnSuccessListener { result ->
                    var tmpList = arrayOf<String>(
                        "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -",
                        "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -")

                    statusList.clear()
                    for (document in result)
                    {
                        val item = StatusList(document["num"] as Long, document["reason"] as String, document["state"] as Boolean, document["type"] as String)
                        statusList.add(item)
                    }
                    for (i in statusList.indices)
                    {
                        if (statusList[i].type == "washer")
                        {
                            if (statusList[i].state == true)
                            {
                                if (statusList[i].reason == "working")
                                    tmpList[i] = statusList[i].num.toString() + "??? ????????? / ?????? - ???????????? / ?????????"
                                else
                                    tmpList[i] = statusList[i].num.toString() + "??? ????????? / ?????? - ???????????? / ????????? X"

                            }
                            else if (statusList[i].state == false)
                            {
                                tmpList[i] = statusList[i].num.toString() + "??? ????????? / ?????? - ??????(" + statusList[i].reason + ")"
                            }
                        }
                        else if (statusList[i].type == "drier")
                        {
                            if (statusList[i].state == true)
                            {
                                if (statusList[i].reason == "working")
                                    tmpList[i] = statusList[i].num.toString() + "??? ????????? / ?????? - ???????????? / ?????????"
                                else
                                    tmpList[i] = statusList[i].num.toString() + "??? ????????? / ?????? - ???????????? / ????????? X"
                            }
                            else if (statusList[i].state == false)
                            {
                                tmpList[i] = statusList[i].num.toString() + "??? ????????? / ?????? - ??????(" + statusList[i].reason + ")"
                            }
                        }
                    }
                    var dlg = AlertDialog.Builder(this@ManagerActivity)
                    dlg.setTitle("?????? ?????? ???????????????")
                    dlg.setItems(tmpList) { dialog, which ->
                    }
                    dlg.setPositiveButton("??????", null)
                    dlg.show()
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, "?????? ??????", Toast.LENGTH_SHORT).show()
                }
        }

        //?????? ?????? ??????
        btnCheckError.setOnClickListener {
            db.collection("Error")
                .get()
                .addOnSuccessListener { result ->
                    errorList.clear()
                    var tmpList = arrayOf<String>(
                        "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -",
                        "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -", "-     -     -     -     -")

                    for (document in result)
                    {
                        var item = ErrorList(document["num"] as Long, document["message"] as String, document["type"] as String)
                        errorList.add(item)
                    }
                    if (errorList.isEmpty())
                    {
                        tmpList[0] = "????????? ????????? ????????????"
                        for (i in 1..7)
                            tmpList[i] = ""
                    }
                    else
                    {
                        for (i in errorList.indices)
                        {
                            if (errorList[i].type == "washer")
                            {
                                tmpList[i] = errorList[i].num.toString() + "??? ????????? ?????? ????????? : " + errorList[i].message
                            }
                            else if (errorList[i].type == "drier")
                            {
                                tmpList[i] = errorList[i].num.toString() + "??? ????????? ?????? ????????? : " + errorList[i].message
                            }
                        }
                    }
                    var dlg = AlertDialog.Builder(this@ManagerActivity)
                    dlg.setTitle("????????? ?????? ????????? ??????????????????")
                    dlg.setItems(tmpList) { dialog, which ->
                    }
                    dlg.setPositiveButton("??????", null)
                    dlg.show()
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(applicationContext, "Fail", Toast.LENGTH_SHORT).show()
                }
        }
    }
}


class MachineList(var num:Long, var type:String)
class StatusList(var num:Long, var reason:String, var state:Boolean, var type:String)
class ErrorList(var num:Long, var message:String, var type:String)
class MachineUser(var book:ArrayList<String>)
class AllMachineUser(var book:ArrayList<String>, var num:Long, var type:String)