package com.ionhax.androidalertapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class myAdapter(private val dataHolder: ArrayList<Model>) :
    RecyclerView.Adapter<myAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.remainder, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = dataHolder[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int {
        return dataHolder.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.txtTitle)
        private val dateTextView: TextView = itemView.findViewById(R.id.txtDate)
        private val timeTextView: TextView = itemView.findViewById(R.id.txtTime)

        fun bind(model: Model) {
            titleTextView.text = model.title
            dateTextView.text = model.date
            timeTextView.text = model.time
        }
    }
}