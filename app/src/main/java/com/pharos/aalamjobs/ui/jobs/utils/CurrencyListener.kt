package com.pharos.aalamjobs.ui.jobs.utils

import com.pharos.aalamjobs.data.responses.dialog.CurrencyResponse

interface CurrencyListener {
    fun setCurrency(currency: CurrencyResponse)
    fun getCurrencyError(code: Int?)
}