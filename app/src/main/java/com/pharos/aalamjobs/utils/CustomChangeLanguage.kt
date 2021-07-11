package com.pharos.aalamjobs.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.databinding.CustomChangeLanguageBinding

class CustomChangeLanguage(
    private val currentLang: String?,
    private val listener: ChangeLangClickListener
) : DialogFragment() {

    private var _binding: CustomChangeLanguageBinding? = null
    private val binding: CustomChangeLanguageBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CustomChangeLanguageBinding.inflate(inflater, container, false)

        when (currentLang) {
            "en" -> {
                binding.kyrgyzButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorGreenBrand
                    )
                )
            }
            "tr" -> {
                binding.englishButton.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorGreenBrand
                    )
                )
            }
                "ru" -> {
                    binding.englishButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorGreenBrand
                        )
                    )
                }
                else -> {
                    binding.kyrgyzButton.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorGreenBrand
                        )
                    )
                }
            }


        binding.topButtonDialog.setOnClickListener {
            dismiss()
        }


        binding.kyrgyzButton.setOnClickListener {
            if (currentLang != "ky")
                listener.onLanguageChange("ky")
            dismiss()
        }

        binding.englishButton.setOnClickListener {
            if (currentLang != "en")
                listener.onLanguageChange("en")
            dismiss()
        }

        binding.russianButton.setOnClickListener {
            if (currentLang != "ru")
                listener.onLanguageChange("ru")
            dismiss()
        }

        binding.turkishButton.setOnClickListener {
            if (currentLang != "tr")
                listener.onLanguageChange("tr")
            dismiss()
        }

        return binding.root
    }
//
//    override fun getTheme(): Int {
//        return R.style.ThemeOverlay_AppCompat_Dialog_Alert
//    }

    interface ChangeLangClickListener {
        fun onLanguageChange(lang: String)
    }

}