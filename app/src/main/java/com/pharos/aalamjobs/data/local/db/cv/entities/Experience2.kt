package com.pharos.aalamjobs.data.local.db.cv.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "experience_table2")
data class Experience2(
val company: String?,
val date_from: String?,
val date_to: String?,
val position: String?,
val responsibilities: String?,
val city: String?,
val country: String?
){
    @PrimaryKey
    var id: Int = 1

}