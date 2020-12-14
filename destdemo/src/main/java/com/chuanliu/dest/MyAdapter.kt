package com.chuanliu.dest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val con: Context, var data : ArrayList<String>) : RecyclerView.Adapter<MyAdapter.MyVH>() {
    private var listData : ArrayList<String> = data
    private var context : Context = con
    class MyVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        public var textItem : TextView = itemView.findViewById<TextView>(R.id.textItem)
    }

    override fun getItemCount(): Int {
        if (listData != null)
            return listData!!.size
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        return MyVH(LayoutInflater.from(context).inflate(R.layout.item, null, false))
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        holder.textItem.text = listData[position]
//        if (position % 3 == 0) {
//            holder.textItem.textSize = 60.0F
//        }
    }
}