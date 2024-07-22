package com.example.vocabularium.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularium.databinding.DefinitionItemBinding
import com.example.vocabularium.databinding.LevelsCardViewBinding
import com.example.vocabularium.databinding.MeaningItemBinding
import com.example.vocabularium.retrofit.DictionaryResponse
import com.example.vocabularium.retrofit.Meanings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WordMeaningsRVAdapter@Inject constructor(@ApplicationContext val context: Context,

                                               val meaningsData: List<Meanings>):RecyclerView.Adapter<WordMeaningsRVAdapter.MeaningsVH>() {


    inner class MeaningsVH(meaningItemBinding: MeaningItemBinding):RecyclerView.ViewHolder(meaningItemBinding.root){
        val meaningItem:MeaningItemBinding
        init {
            meaningItem = meaningItemBinding
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningsVH {
        val layoutInflater = LayoutInflater.from(context)
        val meaningsBinding = MeaningItemBinding.inflate(layoutInflater,parent,false)
        return MeaningsVH(meaningsBinding)
    }

    override fun getItemCount(): Int {
        return meaningsData.size
    }

    override fun onBindViewHolder(holder: MeaningsVH, position: Int) {
        val positionData = meaningsData[position]
        holder.meaningItem.typeName.text = positionData.typeOfWord
        val definitionAdapter = DefinitionsRVAdapter(context,positionData.definitions)
        holder.meaningItem.definitionsRV.layoutManager = LinearLayoutManager(context)
        holder.meaningItem.definitionsRV.adapter = definitionAdapter
        holder.meaningItem.definitionsRV.setHasFixedSize(true)

    }
}