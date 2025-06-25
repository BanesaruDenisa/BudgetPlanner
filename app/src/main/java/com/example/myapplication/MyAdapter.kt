package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyAdapter(
    private val context: Context,
    private var dataList: List<DataClass>,
    private val onItemClick: (DataClass) -> Unit
) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dataList[position]
        holder.recTitle.text = item.title
        holder.recDesc.text = item.dataDesc
        holder.recBudg.text = item.dataBudg
        holder.recDate.text = item.dataDate

        Glide.with(context).load(item.dataImage).into(holder.recImage)

        holder.recCard.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun searchDataList(searchList: List<DataClass>) {
        dataList = searchList
        notifyDataSetChanged()
    }
}

class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val recImage: ImageView = itemView.findViewById(R.id.recImage)
    val recTitle: TextView = itemView.findViewById(R.id.recTitle)
    val recDesc: TextView = itemView.findViewById(R.id.recDesc)
    val recBudg: TextView = itemView.findViewById(R.id.recBudg)
    val recDate: TextView = itemView.findViewById(R.id.recDate)
    val recCard: CardView = itemView.findViewById(R.id.recCard)
}