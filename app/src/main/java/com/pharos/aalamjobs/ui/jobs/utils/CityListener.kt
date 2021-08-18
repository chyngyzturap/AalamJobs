package com.pharos.aalamjobs.ui.jobs.utils

import com.pharos.aalamjobs.data.responses.dialog.CityResponse


interface CityListener {
    fun setCities(jobs: CityResponse)
    fun getCityError(code: Int?)
}