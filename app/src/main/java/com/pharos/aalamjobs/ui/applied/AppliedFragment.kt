package com.pharos.aalamjobs.ui.applied

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.pharos.aalamjobs.R

class AppliedFragment : Fragment() {

    companion object {
        fun newInstance() = AppliedFragment()
    }

    private lateinit var viewModel: AppliedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AppliedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}