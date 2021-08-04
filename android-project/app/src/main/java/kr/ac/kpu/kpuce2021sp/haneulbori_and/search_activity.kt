package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_search.*

class search_activity : AppCompatActivity() {

    val DB: FirebaseFirestore = Firebase.firestore
    val laundryCollectionRef = DB.collection("laundry")
    val laundries = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)



        laundryCollectionRef.get()
            .addOnSuccessListener {
                for (doc in it){
                    laundries.add(doc.id.toString())
                }
                laundryList.adapter = MyCustomAdapter(this, laundries)
                laundryList.setOnClickListener {

                }
            }
            .addOnFailureListener{
                Log.w("MainActivity", "Error getting documents: ")
            }
    }

    private class MyCustomAdapter(context: Context, data: List<String>): BaseAdapter(){
        private val mContext: Context = context
        private val mdata = data

        override fun getCount(): Int {
            return mdata.size
        }

        override fun getItem(position: Int): Any {
            val selectItem = mdata.get(position)
            return selectItem
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val rowMain = layoutInflater.inflate(R.layout.laundry_iteam, parent, false)

            val nameTextView = rowMain.findViewById<TextView>(R.id.laundryId)
            nameTextView.text = mdata.get(position)
            val positionTextView = rowMain.findViewById<TextView>(R.id.laundryInfo)
            positionTextView.text = "순서: " + position

            return rowMain
        }

    }

}

