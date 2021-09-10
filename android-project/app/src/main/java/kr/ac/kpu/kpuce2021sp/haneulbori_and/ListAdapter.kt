package kr.ac.kpu.kpuce2021sp.haneulbori_and

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(val itemList: ArrayList<ListLayout>): RecyclerView.Adapter<ListAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        holder.name.text = itemList[position].name
        holder.num.text = itemList[position].number.toString()
        var tmp=itemList[position].bookList.toString()
        var r1=IntRange(0,16)
        var r2=IntRange(30,34)
        if(tmp.contains("d"))
        {
            var tmp1=tmp.slice(r1)+" ~ "+tmp.slice(r2)+"   "+tmp[51].toString()+"번 건조기]"
            holder.book.text = tmp1
        }
        else if(tmp.contains("w"))
        {
            var tmp1=tmp.slice(r1)+" ~ "+tmp.slice(r2)+"   "+tmp[51].toString()+"번 세탁기]"
            holder.book.text = tmp1
        }
        //holder.finishTime.text = itemList[position].finishTime
        //holder.type.text = itemList[position].type
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val num: TextView = itemView.findViewById(R.id.tvNum)
        val book: TextView = itemView.findViewById(R.id.tvBook)
        //val finishTime: TextView = itemView.findViewById(R.id.tvFinishTime)
        //val type: TextView = itemView.findViewById(R.id.tvType)
    }
}