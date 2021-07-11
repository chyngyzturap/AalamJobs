package com.pharos.aalamjobs.ui.jobs.utils

interface SearchListener {

    fun getCountryId(idCountry: Int, nameCountry: String)
    fun getCityId(idCity: Int, nameCity: String)
    fun getSectorId(idSector: Int, nameSector: String)
    fun getCurrencySign(idCurrency: Int, currencySign: String)
    fun getEmplTypeId(idEmplType: Int, nameEmplType: String)

}