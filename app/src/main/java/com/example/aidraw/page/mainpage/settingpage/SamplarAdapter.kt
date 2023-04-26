package com.example.aidraw.page.mainpage.settingpage

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aidraw.Bean.SamplarBean
import com.example.aidraw.R
import com.example.aidraw.databinding.ItemSamplarBinding
import com.example.aidraw.util.ExUtil

class SamplarAdapter(
    private val samplars: List<SamplarBean>,
    private val context: Context,
    private val itemOnClickListner: ((view: View , index: Int) -> Unit)? = null): RecyclerView.Adapter<SamplarAdapter.SamplarViewHolder>() {

    inner class SamplarViewHolder(binding: ItemSamplarBinding) : RecyclerView.ViewHolder(binding.root){
        val samplarName: TextView =  binding.samplarName
        val samplarSelectedIcon = binding.samplarSelectedIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SamplarViewHolder {
        val binding: ItemSamplarBinding =  ItemSamplarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val viewHolder = SamplarViewHolder(binding)
        viewHolder.itemView.setOnClickListener {
            itemOnClickListner?.let {
                it(viewHolder.itemView , viewHolder.bindingAdapterPosition)
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return samplars.size
    }

    override fun onBindViewHolder(holder: SamplarViewHolder, position: Int) {
        val adapterPosition =  holder.bindingAdapterPosition
        val samplarBean = samplars[adapterPosition]
        holder.samplarName.text = samplarBean.name
        if(samplarBean.selected){
            holder.samplarSelectedIcon.visibility = View.VISIBLE
            ExUtil.setLinearGradientText(holder.samplarName , context)
        }else{
            holder.samplarSelectedIcon.visibility = View.GONE
            holder.samplarName.viewTreeObserver.addOnDrawListener {
                val shader = LinearGradient(
                    0f, 0f,
                    holder.samplarName.width.toFloat(),
                    holder.samplarName.height.toFloat(),
                    intArrayOf(Color.BLACK , Color.BLACK) , null, Shader.TileMode.CLAMP
                )
                holder.samplarName.paint.setShader(shader)
                holder.samplarName.invalidate()
            }



        }


    }

}