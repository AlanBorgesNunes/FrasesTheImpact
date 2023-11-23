package com.app.frasestheimpact.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.frasestheimpact.databinding.PagerItemBinding
import com.app.frasestheimpact.utlis.DadosDaFrase

class AdapterFrases(private val context: Context, private var list: List<DadosDaFrase>, val clickShare: ClickShare):
RecyclerView.Adapter<AdapterFrases.ViewHolder>(){


    interface ClickShare{
        fun clickShare(daFrase: DadosDaFrase)
    }

    fun updateList(list: MutableList<DadosDaFrase>){
        this.list = list
        notifyDataSetChanged()
    }
    inner class ViewHolder(private val binding: PagerItemBinding): RecyclerView.ViewHolder(binding.root){
         fun bind(daFrase: DadosDaFrase){
             binding.receiveFrase.text = daFrase.frase
             binding.receiveAutor.text = daFrase.nomeAutor

             binding.share.setOnClickListener {
                 clickShare.clickShare(daFrase)
             }
         }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = PagerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curentFrase = list[position]
        holder.bind(curentFrase)
    }
}