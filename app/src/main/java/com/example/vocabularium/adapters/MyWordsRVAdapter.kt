package com.example.vocabularium.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularium.R
import com.example.vocabularium.databinding.MyWordsItemBinding
import com.example.vocabularium.exceptions.MyWordsSingleton
import com.example.vocabularium.firebase.FirebaseWords
import com.example.vocabularium.models.AppWords
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MyWordsRVAdapter@Inject constructor(@ApplicationContext val context: Context,val data:ArrayList<FirebaseWords>):RecyclerView.Adapter<MyWordsRVAdapter.MyWordsVH>() {
    var myWordValue: ((FirebaseWords)->Unit)? = null
    var myWordSingleton =MyWordsSingleton
    inner class MyWordsVH(myWordsItemBinding: MyWordsItemBinding):RecyclerView.ViewHolder(myWordsItemBinding.root){
        var myWordsBinding: MyWordsItemBinding
        init {
            myWordsBinding = myWordsItemBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyWordsVH {
        val layoutInflater = LayoutInflater.from(context)
        val myWordsBinding = MyWordsItemBinding.inflate(layoutInflater,parent,false)
        return MyWordsVH(myWordsBinding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyWordsVH, position: Int) {
        var itemData = data.get(position)
        holder.myWordsBinding.myWordsName.text = itemData.wordName
        holder.myWordsBinding.myWordsLevel.text = when(itemData.wordLevel){
            "A1"->"Beginner"
            "A2"->"Elementary"
            "B1"->"Intermediate"
            "B2"->"Upper Intermediate"
            "C1"->"Advanced"
            else ->itemData.wordLevel
        }
        if (itemData.wordState == "true")holder.myWordsBinding.myWordsTick.setImageResource(R.drawable.my_words_tick_positive)
        else holder.myWordsBinding.myWordsTick.setImageResource(R.drawable.my_words_tick_negative)
        holder.itemView.setOnClickListener {
            myWordValue?.invoke(itemData)
            myWordSingleton.myWord = itemData
        }
    }
}