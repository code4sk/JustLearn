package com.code4sk.justlearn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordRecyclerViewHolder(view: View): RecyclerView.ViewHolder(view) {
//    val text = view.findViewById<TextView>(R.id.wordText)
    val text = view.findViewById<CheckedTextView>(R.id.wordText) as CheckedTextView
}

class WordsAdapter(var recList: ArrayList<WordsActivity.Duo>): RecyclerView.Adapter<WordRecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_word, parent, false)
        return WordRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(recList.isNotEmpty()) recList.size else 0
    }

    fun loadNewData(newList: ArrayList<WordsActivity.Duo>){
        recList = newList
        notifyDataSetChanged()
    }

    fun getWord(position: Int): WordsActivity.Duo {
        return recList[position]
    }

    override fun onBindViewHolder(holder: WordRecyclerViewHolder, position: Int) {
        holder.text.text = recList[position].name

        if(recList[position].check){
            holder.text.isChecked = recList[position].check
            holder.text.setCheckMarkDrawable(R.drawable.our_checkbox)

        } else {
            holder.text.isChecked = false
            holder.text.setCheckMarkDrawable(null)
        }

    }
}