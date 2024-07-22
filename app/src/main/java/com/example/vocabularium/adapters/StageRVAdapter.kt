package com.example.vocabularium.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.vocabularium.R
import com.example.vocabularium.animations.Attention
import com.example.vocabularium.animations.Zoom
import com.example.vocabularium.databinding.LearningProgreesBarBinding
import com.example.vocabularium.exceptions.MySingleton
import com.example.vocabularium.models.AppWords
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class StageRVAdapter @Inject constructor(@ApplicationContext val context: Context,val data:List<List<AppWords>>)
    :RecyclerView.Adapter<StageRVAdapter.StageRVAdapterVH>(){
        val myObject = MySingleton
        var learntNumbers = ArrayList<Int>()
        init {
            learntNumbers = assignProgress(data)
        }
    fun assignProgress(data:List<List<AppWords>>):ArrayList<Int>{
        val learntNumbers = ArrayList<Int>()
        data.forEach {list->
            val number = list.filter { it.wordState == true }.size
            learntNumbers.add(number)
        }
        return learntNumbers
    }
    inner  class StageRVAdapterVH(stageBinding:LearningProgreesBarBinding):RecyclerView.ViewHolder(stageBinding.root) {
        val learningProcess: LearningProgreesBarBinding
        init {
            this.learningProcess = stageBinding

            learningProcess.progressBar.progress = 0
        }
        
        fun animate(position: Int){
            val animation = Zoom()
            animation.InLeft(learningProcess.root).apply {
                duration = 700
                start()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageRVAdapterVH {
        val layoutInflater = LayoutInflater.from(context)
        val learningProcess = LearningProgreesBarBinding.inflate(layoutInflater,parent,false)

        return  StageRVAdapterVH(learningProcess)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: StageRVAdapterVH, position: Int) {
        val itemData = data.get(position)
        val complationRate = learntNumbers.get(position) * 10
        CoroutineScope(Dispatchers.Main).launch {
            holder.learningProcess.progressBar.setProgress(0)
            delay(200)
            if (complationRate != 0){
                holder.learningProcess.progressBar.progressDrawable = ResourcesCompat.getDrawable(context.resources,R.drawable.custom_progress_bar,null)
                holder.learningProcess.progressBar.setProgress(complationRate)
            }
            else holder.learningProcess.progressBar.progressDrawable = ResourcesCompat.getDrawable(context.resources,R.drawable.custom_progress_bar2,null)
        }
        holder.learningProcess.positionText.text = "${complationRate / 10} / ${context.resources.getInteger(R.integer.sizeOfStage)}"
        holder.learningProcess.stageName.text = "Stage ${position + 1}"
        holder.learningProcess.completionRateText.text = "%${complationRate} Completed"
        holder.animate(position)
        holder.itemView.setOnClickListener {
            addList(itemData)
            myObject.stageValue = position + 1
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_sideActivity)
        }
    }
    fun addList(list: List<AppWords>){
        myObject.dataList = list
    }


}


