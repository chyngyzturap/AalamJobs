package com.pharos.aalamjobs.data.local.db.cv.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "links_table")
data class Achievements(
val links: String,
val achievements: String,
val skills: String
){
    @PrimaryKey
    var id: Int = 1

}