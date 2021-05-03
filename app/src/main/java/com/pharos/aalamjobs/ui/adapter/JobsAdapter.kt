package com.pharos.aalamjobs.ui.adapter

import android.content.DialogInterface
import android.service.autofill.OnClickAction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.responses.Jobs
import com.pharos.aalamjobs.databinding.JobsItemBinding

class JobsAdapter() : RecyclerView.Adapter<JobsAdapter.JobsViewHolder>() {

    private var _binding: JobsItemBinding? = null
    private var jobsList: MutableList<Jobs> = mutableListOf()

    public fun addData(data: MutableList<Jobs>){
        jobsList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        _binding = JobsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return jobsList.size
    }

    inner class JobsViewHolder(private val binding: JobsItemBinding): RecyclerView.ViewHolder(binding.root){

        fun onBind(position: Int) {
            binding.jobNameLocation.text = jobsList[position].location
binding.jobTitle.text = jobsList[position].title
            binding.jobsDate.text = jobsList[position].start_date
        }
    }
}