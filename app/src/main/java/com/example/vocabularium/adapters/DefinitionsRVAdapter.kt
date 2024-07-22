package com.example.vocabularium.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularium.databinding.DefinitionItemBinding
import com.example.vocabularium.retrofit.Definition
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefinitionsRVAdapter @Inject constructor(@ApplicationContext val context: Context,
                                               val definitionData: List<Definition>):RecyclerView.Adapter<DefinitionsRVAdapter.DefinitionsVH>() {
    inner class DefinitionsVH(definitionItemBinding: DefinitionItemBinding):RecyclerView.ViewHolder(definitionItemBinding.root){
         val definitionBinding:DefinitionItemBinding
         init {
             definitionBinding = definitionItemBinding
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionsVH {
       val layoutInflater = LayoutInflater.from(context)
        val definitionBinding = DefinitionItemBinding.inflate(layoutInflater,parent,false)
        return DefinitionsVH(definitionBinding)
    }

    override fun getItemCount(): Int {
       return definitionData.size
    }

    override fun onBindViewHolder(holder: DefinitionsVH, position: Int) {
        val positionData = definitionData.get(position)
        holder.definitionBinding.definition.text =positionData.definition
        if (positionData.example!= null)holder.definitionBinding.example.text = "       -"+positionData.example

    }

}