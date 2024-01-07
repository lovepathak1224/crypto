package com.example.crypto.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crypto.R
import com.example.crypto.databinding.RecyclerviewitemBinding
import com.example.crypto.models.Model

class Bitcoinadapter(
    private val context: Context,
    private val data: ArrayList<Model>,
    private var onItemClick: (position: Int) -> Unit = {}
) : RecyclerView.Adapter<Bitcoinadapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerviewitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setOnItemClickListener(listener: (position: Int) -> Unit) {
        onItemClick = listener
    }

    inner class ViewHolder(private val binding: RecyclerviewitemBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(model: Model) {
            binding.apply {
                name.text = model.name
                symbol.text = model.symbol
                price.text = model.price
                Glide.with(context)
                    .load(model.imageUrl)
                    .placeholder(R.drawable.bitcoin)
                    .into(image)
            }
        }

        override fun onClick(v: View?) {
            onItemClick(adapterPosition)
        }
    }
}
