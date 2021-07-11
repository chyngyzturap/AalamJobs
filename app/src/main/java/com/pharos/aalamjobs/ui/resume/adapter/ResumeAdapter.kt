package com.pharos.aalamjobs.ui.resume.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pharos.aalamjobs.data.local.db.cv.models.CvModelResponse
import com.pharos.aalamjobs.databinding.ResumesItemBinding

class ResumeAdapter(
    private val listener: CvClickListener
) : ListAdapter<CvModelResponse, ResumeAdapter.CvViewHolder>(DIFF) {

    private var _binding: ResumesItemBinding? = null
    private var resumeList: MutableList<CvModelResponse> = mutableListOf()

    public fun addData(data: MutableList<CvModelResponse>) {
        resumeList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CvViewHolder {
        _binding = ResumesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CvViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: CvViewHolder, position: Int) {
        holder.onBind(position)
    }

//    override fun getItemCount(): Int {
//        return jobsList.size
//    }

    fun getItemAtPos(position: Int): CvModelResponse {
        return getItem(position)
    }

    inner class CvViewHolder(private val binding: ResumesItemBinding) :
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

            binding.tvUserTitle.text = current.firstname + " " + current.lastname
            binding.tvDate.text = current.updated_at.split("T")[0]


            binding.root.setOnClickListener {
                listener.onCvClick(current.id)
            }

        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<CvModelResponse>() {
            override fun areItemsTheSame(oldItem: CvModelResponse, newItem: CvModelResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CvModelResponse, newItem: CvModelResponse): Boolean {
                return oldItem.position == newItem.position
            }

        }
    }

    interface CvClickListener {
        fun onCvClick(cvId: Int)
    }
}