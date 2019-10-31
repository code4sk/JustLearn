package com.code4sk.justlearn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class RecyclerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val text = view.findViewById<TextView>(R.id.recordingText)
}

class RecordingsAdapter(var recList: ArrayList<File>): RecyclerView.Adapter<RecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_recording, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(recList.isNotEmpty()) recList.size else 0
    }

    fun loadNewData(newList: ArrayList<File>){
        recList = newList
        notifyDataSetChanged()
    }

    fun getRecording(position: Int): File{
        return recList[position]
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.text.text = recList[position].name
    }
}