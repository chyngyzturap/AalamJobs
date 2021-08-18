package com.pharos.aalamjobs.ui.jobs.utils

import com.pharos.aalamjobs.data.responses.dialog.EmploymentTypeResponse


interface EmplTypeListener {
    fun setEmplTypes(emplTypes: EmploymentTypeResponse)
    fun getEmplTypesError(code: Int?)
}