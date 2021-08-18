package com.pharos.aalamjobs.ui.jobs.utils

import com.pharos.aalamjobs.data.responses.dialog.SectorResponse


interface SectorListener {
    fun setSectors(jobs: SectorResponse)
    fun getSectorError(code: Int?)
}