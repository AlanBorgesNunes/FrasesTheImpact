package com.app.frasestheimpact.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.app.frasestheimpact.R

class AdapterVp(private val context: Context, private val imageList: List<Int>):
RecyclerView.Adapter<AdapterVp.ImageViewHolder>(){

    inner class ImageViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.pager_item, parent, false)
        return ImageViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if(imageList.isNotEmpty()) Int.MAX_VALUE else 0
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val realPosition = position % imageList.size
    }

}