package com.pharos.aalamjobs.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pharos.aalamjobs.data.responses.CurrencyResponseItem
import com.pharos.aalamjobs.databinding.ListItemBinding

class CurrencyAdapter(
    private val listener: CurrencyClickListener
) : ListAdapter<CurrencyResponseItem, CurrencyAdapter.JobsViewHolder>(DIFF) {

    private var _binding: ListItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        _binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.onBind(position)
    }


    fun getItemAtPos(position: Int): CurrencyResponseItem {
        return getItem(position)
    }

    inner class JobsViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)

            binding.tvCounter.text = current.sign

            binding.root.setOnClickListener {
                listener.onCurrencyCLick(position)
            }

        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CurrencyResponseItem>() {
            override fun areItemsTheSame(oldItem: CurrencyResponseItem, newItem: CurrencyResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CurrencyResponseItem, newItem: CurrencyResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    interface CurrencyClickListener {
        fun onCurrencyCLick(position: Int)
    }
}