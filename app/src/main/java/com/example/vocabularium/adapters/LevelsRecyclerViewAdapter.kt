package com.example.vocabularium.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.vocabularium.R
import com.example.vocabularium.animations.Attention
import com.example.vocabularium.databinding.LevelsCardViewBinding
import com.example.vocabularium.fragments.HomeFragment
import com.example.vocabularium.fragments.HomeFragmentViewModel
import com.example.vocabularium.models.LanguageLevels
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LevelsRecyclerViewAdapter@Inject constructor(@ApplicationContext private val context: Context,
                                                   private var levelsData: ArrayList<LanguageLevels>):
    RecyclerView.Adapter<LevelsRecyclerViewAdapter.LevelsRcViewHolder>() {
    var onItemClick:((LanguageLevels)->Unit)? = null
    private var selectedItem =levelsData.get(0)
    inner class LevelsRcViewHolder(levelsCardViewBinding: LevelsCardViewBinding):
        RecyclerView.ViewHolder(levelsCardViewBinding.root){
            var levelsCardView: LevelsCardViewBinding
            init {
                this.levelsCardView = levelsCardViewBinding
            }
        val animation = Attention()
        fun animate(){
            animation.Tada(levelsCardView.root).apply {
                duration = 5000
                start()
            }
        }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelsRcViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val levelsCardViewBinding = LevelsCardViewBinding.inflate(layoutInflater,parent,false)
        return LevelsRcViewHolder(levelsCardViewBinding)
    }

    override fun getItemCount(): Int {
        return levelsData.size
    }

    override fun onBindViewHolder(holder: LevelsRcViewHolder, position: Int) {
        var level = levelsData.get(position)
        holder.levelsCardView.levelsObject = level
        holder.levelsCardView.levelImage.setImageResource(level.levelPictureId)
        holder.animate()
        if (level.levelName == selectedItem.levelName){
            holder.levelsCardView.levelCardView.setBackgroundResource(R.drawable.background1_selected)
        }else holder.levelsCardView.levelCardView.setBackgroundResource(R.drawable.background1)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(level)
            selectedItem = level
            notifyDataSetChanged()
        }
    }
}