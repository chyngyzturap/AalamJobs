package com.pharos.aalamjobs.data.local.db.cv.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_req_table")
data class JobRequirements (
    val salary_expectations: Int?,
    val employment_type: Int?,
    val position: String?,
    val date_can_start: String?,
    val bio: String?
) {
    @PrimaryKey
    var id: Int = 1
}