package com.pharos.aalamjobs.ui.jobs

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pharos.aalamjobs.R
import com.pharos.aalamjobs.ui.jobs.search.location.CityDialogFragment
import com.pharos.aalamjobs.ui.jobs.search.location.CountryDialogFragment
import com.pharos.aalamjobs.ui.jobs.search.sector.SectorDialogFragment
import com.pharos.aalamjobs.ui.jobs.utils.SearchListener
import com.pharos.aalamjobs.ui.main.MainActivity
import com.pharos.aalamjobs.ui.splash.SplashActivity
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

    override fun getCurrencySign(idCurrency: Int,currencySign: String) {

    }

    override fun getEmplTypeId(idEmplType: Int, nameEmplType: String) {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_search_detail)

        tv_country.setEndIconOnClickListener {
            val countryDialogFragment = CountryDialogFragment(this)
            val manager = supportFragmentManager
            countryDialogFragment.show(manager, "countryDialog")
        }
        tv_city.setEndIconOnClickListener {
            val cityDialogFragment = CityDialogFragment(this)
            val manager = supportFragmentManager
            cityDialogFragment.show(manager, "cityDialog")
        }
        tv_industry.setEndIconOnClickListener {
            val sectorDialogFragment = SectorDialogFragment(this)
            val manager = supportFragmentManager
            sectorDialogFragment.show(manager, "sectorDialog")
        }

        btn_applied.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("countryId", mId)
            intent.putExtra("cityId", mIdCity)
            intent.putExtra("sectorId", mIdSector)
            startActivity(intent)
            finish()
        }

    }

    private fun authAlertDialog(){
        val mAlertDialog = AlertDialog.Builder(this)
//            mAlertDialog.setIcon(R.mipmap.ic_launcher_round) //set alertdialog icon

        mAlertDialog.setPositiveButtonIcon(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_done_24_green
            )
        )
        mAlertDialog.setNegativeButtonIcon(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_cancel_24_red
            )
        )
        mAlertDialog.setTitle("Please sign up to get access") //set alertdialog title
//        mAlertDialog.setMessage(R.string.txt_logout_before_remind) //set alertdialog message
        mAlertDialog.setPositiveButton("Sign Up ") { dialog, id ->
            ActivityCompat.finishAffinity(this)
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
        }
        mAlertDialog.setNegativeButton("Cancel") { dialog, id ->
            null
        }
        mAlertDialog.show()
    }


}