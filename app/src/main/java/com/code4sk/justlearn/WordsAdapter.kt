package com.code4sk.justlearn

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WordRecyclerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val text = view.findViewById<TextView>(R.id.wordText)
}

class WordsAdapter(var recList: ArrayList<String>): RecyclerView.Adapter<WordRecyclerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_word, parent, false)
        return WordRecyclerViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(recList.isNotEmpty()) recList.size else 0
    }

    fun loadNewData(newList: ArrayList<String>){
        recList = newList
        notifyDataSetChanged()
    }

    fun getWord(position: Int): String{
        return recList[position]
    }

    override fun onBindViewHolder(holder: WordRecyclerViewHolder, position: Int) {
        holder.text.text = recList[position]
    }
}