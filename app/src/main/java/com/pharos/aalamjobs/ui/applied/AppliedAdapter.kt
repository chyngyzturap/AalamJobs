package com.pharos.aalamjobs.ui.applied

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pharos.aalamjobs.databinding.AppliedItemBinding
import com.pharos.aalamjobs.ui.applied.model.AppliedJobsResponseItem

class AppliedAdapter(
    private val listener: JobClickListener
) : ListAdapter<AppliedJobsResponseItem, AppliedAdapter.JobsViewHolder>(DIFF) {

    private var _binding: AppliedItemBinding? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        _binding = AppliedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.onBind(position)
    }

    fun getItemAtPos(position: Int): AppliedJobsResponseItem {
        return getItem(position)
    }

    inner class JobsViewHolder(private val binding: AppliedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun onBind(position: Int) {
            val current = getItemAtPos(position)
            binding.jobTitle.text = current.job.title
            binding.jobNameCompany.text = current.job.organization.name
            binding.jobNameLocation.text = current.job.city.name.en.toString() + ", " +
                    current.job.city.country.name.en
            binding.jobsDate.text = current.job.published_date.split("T")[0]
            binding.jobsSalary.text =
                current.job.salary.min.toString() + "-" + current.job.salary.max + current.job.currency.sign

            binding.root.setOnClickListener {
                listener.onJobClick(current.job.id)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<AppliedJobsResponseItem>() {
            override fun areItemsTheSame(
                oldItem: AppliedJobsResponseItem,
                newItem: AppliedJobsResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AppliedJobsResponseItem,
                newItem: AppliedJobsResponseItem
            ): Boolean {
                return oldItem.job.id == newItem.job.id
            }
        }
    }

    interface JobClickListener {
        fun onJobClick(jobId: Int)
        fun addToFavorites(position: Int)
        fun deleteFromFavorites(position: Int)
    }
}