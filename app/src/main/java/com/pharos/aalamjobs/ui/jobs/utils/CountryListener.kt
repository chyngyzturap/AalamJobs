package com.pharos.aalamjobs.ui.jobs.utils

import com.pharos.aalamjobs.data.responses.dialog.CountryResponse


interface CountryListener {
    fun setCountry(jobs: CountryResponse)
    fun getCountryError(code: Int?)
}