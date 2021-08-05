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
        holder.number.text = itemList[position].number
        holder.birth.text = itemList[position].birthday
        holder.email.text = itemList[position].email
        holder.sex.text = itemList[position].sex
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val number: TextView = itemView.findViewById(R.id.tvNumber)
        val email: TextView = itemView.findViewById(R.id.tvEmail)
        val birth: TextView = itemView.findViewById(R.id.tvBirth)
        val sex: TextView = itemView.findViewById(R.id.tvSex)
    }
}