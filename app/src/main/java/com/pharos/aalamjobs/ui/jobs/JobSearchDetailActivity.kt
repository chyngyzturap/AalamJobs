package com.pharos.aalamjobs.ui.jobs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.ui.jobs.search.location.CityDialogDialogFragment
import com.pharos.aalamjobs.ui.jobs.search.location.CountryDialogDialogFragment
import com.pharos.aalamjobs.ui.jobs.search.sector.SectorDialogDialogFragment
import com.pharos.aalamjobs.ui.jobs.utils.SearchListener
import com.pharos.aalamjobs.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_job_search_detail.*

class JobSearchDetailActivity : AppCompatActivity(), SearchListener {
    private var mId: Int = 0
    private var mName: String = ""
    private var mIdCity: Int = 0
    private var mNameCity: String = ""
    private var mIdSector: Int = 0
    private var mNameSector: String = ""

    override fun getCountryId(idCountry: Int, nameCountry: String) {
        mId = idCountry
        mName = nameCountry
        tv_country.editText?.setText(mName)
    }

    override fun getCityId(idCity: Int, nameCity: String) {
        mIdCity = idCity
        mNameCity = nameCity
        tv_city.editText?.setText(mNameCity)
    }

    override fun getSectorId(idSector: Int, nameSector: String) {
        mIdSector = idSector
        mNameSector = nameSector
        tv_industry.editText?.setText(mNameCity)
    }

    override fun getCurrencySign(idCurrency: Int, currencySign: String) {
    }

    override fun getEmplTypeId(idEmplType: Int, nameEmplType: String) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_search_detail)

        tv_country.editText?.setOnClickListener {
            val countryDialogFragment = CountryDialogDialogFragment(this)
            val manager = supportFragmentManager
            countryDialogFragment.show(manager, "countryDialog")
        }
        tv_city.editText?.setOnClickListener {
            val cityDialogFragment = CityDialogDialogFragment(this)
            val manager = supportFragmentManager
            cityDialogFragment.show(manager, "cityDialog")
        }
        tv_industry.editText?.setOnClickListener {
            val sectorDialogFragment = SectorDialogDialogFragment(this)
            val manager = supportFragmentManager
            sectorDialogFragment.show(manager, "sectorDialog")
        }
        btn_applied.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            if (mId != 0) {
                intent.putExtra("countryId", mId)
            }
            if (mIdCity != 0) {
                intent.putExtra("cityId", mIdCity)
            }
            if (mIdSector != 0) {
                intent.putExtra("sectorId", mIdSector)
            }
            startActivity(intent)
            finish()
        }
        tv_clear_all.setOnClickListener {
            tv_salary.editText?.setText("")
            tv_country.editText?.setText("")
            tv_city.editText?.setText("")
            tv_industry.editText?.setText("")
            mId = 0
            mIdCity = 0
            mIdSector = 0
        }
        iv_backpressed.setOnClickListener { onBackPressed() }
    }
}