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
import com.example.aidraw.Bean.SamplerBean
import com.example.aidraw.databinding.ItemSamplerBinding
import com.example.aidraw.util.ExUtil

class SamplerAdapter(
    private val samplers: List<SamplerBean>,
    private val context: Context,
    private val itemOnClickListner: ((view: View , index: Int) -> Unit)? = null): RecyclerView.Adapter<SamplerAdapter.SamplerViewHolder>() {

    inner class SamplerViewHolder(binding: ItemSamplerBinding) : RecyclerView.ViewHolder(binding.root){
        val samplerName: TextView =  binding.samplerName
        val samplerSelectedIcon = binding.samplerSelectedIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SamplerViewHolder {
        val binding: ItemSamplerBinding =  ItemSamplerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = SamplerViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            itemOnClickListner?.let {
                it(viewHolder.itemView , viewHolder.bindingAdapterPosition)
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return samplers.size
    }

    override fun onBindViewHolder(holder: SamplerViewHolder, position: Int) {
        val adapterPosition =  holder.bindingAdapterPosition
        val samplerBean = samplers[adapterPosition]
        holder.samplerName.text = samplerBean.name
        if(samplerBean.selected){
            holder.samplerSelectedIcon.visibility = View.VISIBLE
            ExUtil.setLinearGradientText(holder.samplerName , context)
        }else{
            holder.samplerSelectedIcon.visibility = View.GONE
            holder.samplerName.viewTreeObserver.addOnDrawListener {
                val shader = LinearGradient(
                    0f, 0f,
                    holder.samplerName.width.toFloat(),
                    holder.samplerName.height.toFloat(),
                    intArrayOf(Color.BLACK , Color.BLACK) , null, Shader.TileMode.CLAMP
                )
                holder.samplerName.paint.setShader(shader)
                holder.samplerName.invalidate()
            }



        }


    }

}