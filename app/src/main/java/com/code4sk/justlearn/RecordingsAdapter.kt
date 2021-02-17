package com.code4sk.justlearn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import java.io.File



class RecordingsAdapter(var recList: ArrayList<RecItem>): RecyclerView.Adapter<RecordingsAdapter.RecyclerViewHolder>() {
    inner class RecyclerViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val text = view.findViewById<CheckedTextView>(R.id.recordingText) as CheckedTextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordingsAdapter.RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_recording, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(recList.isNotEmpty()) recList.size else 0
    }

    fun loadNewData(newList: ArrayList<RecItem>){
        recList = newList
        notifyDataSetChanged()
    }

    fun getRecording(position: Int): RecItem{
        return recList[position]
    }

    fun getPosition(name: String) = recList.indexOfFirst { it.file.name == name }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.text.text = recList[position].file.nameWithoutExtension
        if(recList[position].check){
            holder.text.isChecked = true
            holder.text.setCheckMarkDrawable(R.drawable.our_checkbox)
        } else {
            holder.text.isChecked = false
            holder.text.setCheckMarkDrawable(null)
        }
    }
}