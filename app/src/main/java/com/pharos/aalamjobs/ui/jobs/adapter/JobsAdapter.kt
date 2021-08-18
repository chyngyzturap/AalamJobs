package com.pharos.aalamjobs.ui.jobs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.data.model.Jobs
import com.pharos.aalamjobs.databinding.JobsItemBinding

class JobsAdapter(
    private val listener: JobClickListener
) : ListAdapter<Jobs, JobsAdapter.JobsViewHolder>(DIFF) {

    private var _binding: JobsItemBinding? = null
    private var jobsList: MutableList<Jobs> = mutableListOf()

    public fun addData(data: MutableList<Jobs>) {
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

//    override fun getItemCount(): Int {
//        return jobsList.size
//    }

    fun getItemAtPos(position: Int): Jobs {
        return getItem(position)
    }

    inner class JobsViewHolder(private val binding: JobsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val current = getItemAtPos(position)


//            binding.jobNameLocation.text = jobsList[position].city.toString()
//            binding.jobTitle.text = jobsList[position].title
//            binding.jobsDate.text = jobsList[position].published
//            binding.jobsSalary.text = jobsList[position].salary.min.toString() + "-" +
//                    jobsList[position].salary.max + current.salary.currency
//            binding.jobNameCompany.text = jobsList[position].city.name.toString() + ", " +
//                    jobsList[position].city.country.name

            binding.jobTitle.text = current.title
            binding.jobNameCompany.text = current.organization.name
            binding.jobNameLocation.text = current.city.name.en.toString() + ", " +
                    current.city.country.name.en
            binding.jobsDate.text = current.published_date.split("T")[0]
            binding.jobsSalary.text =
                current.salary.min.toString() + "-" + current.salary.max + current.currency.sign



            if (current.favorite){
                binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_filled)

            } else {
                binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
            }



            binding.ivJobFavorite.setOnClickListener{
                current.favorite

                    if (!current.favorite) {
                        current.favorite
                        binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_filled)
                        listener.addToFavorites(current.id)

                    } else {
                        !current.favorite
                        binding.ivJobFavorite.setImageResource(R.drawable.vector_favorite_red_empty)
                        listener.deleteFromFavorites(current.id)

                    }
            }

            binding.root.setOnClickListener {
                listener.onJobClick(current.id)
            }

        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Jobs>() {
            override fun areItemsTheSame(oldItem: Jobs, newItem: Jobs): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Jobs, newItem: Jobs): Boolean {
                return oldItem.title == newItem.title
            }

        }
    }

    interface JobClickListener {
        fun onJobClick(jobId: Int)
        fun addToFavorites(position: Int)
        fun deleteFromFavorites(position: Int)
    }
}