package com.pharos.aalamjobs.utils

import com.pharos.aalamjobs.data.responses.CurrencyResponse


interface CurrencyListener {


    fun setCurrency(currency: CurrencyResponse)
    fun getCurrencyError(code: Int?)


}