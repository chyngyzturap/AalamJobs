package com.pharos.aalamjobs.ui.jobs.utils

import com.pharos.aalamjobs.data.responses.CityResponse


interface CityListener {


    fun setCities(jobs: CityResponse)
    fun getCityError(code: Int?)


}