package com.pharos.aalamjobs.utils.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.databinding.LayoutRequiredSectorsBinding
import com.pharos.aalamjobs.utils.visible

class RequiredSectionsDialogFragment(
    var profileCode: Int, var contactCode: Int,
    var langCode: Int, var jobreqCode: Int
) : DialogFragment() {

    private var _binding: LayoutRequiredSectorsBinding? = null
    private val binding: LayoutRequiredSectorsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutRequiredSectorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (profileCode == 1) {
            binding.btnNavigatePersonals.visible(false)

        } else {
            binding.btnNavigatePersonals.visible(true)
            binding.btnNavigatePersonals.setOnClickListener {
                findNavController().navigate(R.id.personalInformationFragment)
                dismiss()
            }
        }

        if (contactCode == 1) {
            binding.btnNavigateContacts.visible(false)
        } else {
            binding.btnNavigateContacts.visible(true)
            binding.btnNavigateContacts.setOnClickListener {
                findNavController().navigate(R.id.contactInformationFragment)
                dismiss()
            }
        }

        if (langCode == 1) {
            binding.btnNavigateLanguage.visible(false)
        } else {
            binding.btnNavigateLanguage.visible(true)
            binding.btnNavigateLanguage.setOnClickListener {
                findNavController().navigate(R.id.languagesFragment)
                dismiss()
            }
        }

        if (jobreqCode == 1) {
            binding.btnNavigateRequirements.visible(false)
        } else {
            binding.btnNavigateRequirements.visible(true)
            binding.btnNavigateRequirements.setOnClickListener {
                findNavController().navigate(R.id.jobRequirementFragment)
                dismiss()
            }
        }
    }
}