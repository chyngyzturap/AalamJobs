package com.pharos.aalamjobs.data.local.db.cv

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "other_languages_skills_table")
data class OtherLanguageSkills (
        val talking: String?,
        val speaking: String?,
        val reading: String?,
        val writing: String?
){
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null
}
