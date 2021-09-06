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
        holder.book.text = itemList[position].bookList.toString()
        //holder.finishTime.text = itemList[position].finishTime
        //holder.type.text = itemList[position].type
        holder.num.text = itemList[position].number
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val book: TextView = itemView.findViewById(R.id.tvBook)
        //val finishTime: TextView = itemView.findViewById(R.id.tvFinishTime)
        //val type: TextView = itemView.findViewById(R.id.tvType)
        val num: TextView = itemView.findViewById(R.id.tvNum)
    }
}