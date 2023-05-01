package com.example.aidraw.page.mainpage.settingpage

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aidraw.Bean.ModelBean
import com.example.aidraw.databinding.ItemModelBinding
import com.example.aidraw.util.ExUtil

class ModelAdapter(
    private val models: List<ModelBean>,
    private val context: Context,
    private val itemOnClickListner: ((view: View , index: Int) -> Unit)? = null): RecyclerView.Adapter<ModelAdapter.ModelViewHolder>() {

    inner class ModelViewHolder(binding: ItemModelBinding) : RecyclerView.ViewHolder(binding.root){
        val modelName: TextView =  binding.modelName
        val modelSelectedIcon = binding.modelSelectedIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val binding: ItemModelBinding =  ItemModelBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = ModelViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            itemOnClickListner?.let {
                it(viewHolder.itemView , viewHolder.bindingAdapterPosition)
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        val adapterPosition =  holder.bindingAdapterPosition
        val modelBean = models[adapterPosition]
        holder.modelName.text = modelBean.name
        if(modelBean.selected){
            holder.modelSelectedIcon.visibility = View.VISIBLE
            ExUtil.setLinearGradientText(holder.modelName , context)
        }else{
            holder.modelSelectedIcon.visibility = View.GONE
            holder.modelName.viewTreeObserver.addOnDrawListener {
                val shader = LinearGradient(
                    0f, 0f,
                    holder.modelName.width.toFloat(),
                    holder.modelName.height.toFloat(),
                    intArrayOf(Color.BLACK , Color.BLACK) , null, Shader.TileMode.CLAMP
                )
                holder.modelName.paint.setShader(shader)
                holder.modelName.invalidate()
            }



        }


    }

}