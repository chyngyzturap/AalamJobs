package com.pharos.aalamjobs.data.local.db.cv.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pharos.aalamjobs.data.local.db.cv.Converters
import com.pharos.aalamjobs.data.local.db.cv.dao.*
import com.pharos.aalamjobs.data.local.db.cv.entities.*


@Database(
    version = 12,
    entities = [PersonalInfo::class, ContactInfo::class, Social::class,
        Education::class,Education2::class,Education3::class,
        MotherLanguage::class,
        OtherLanguages::class,OtherLanguages2::class,OtherLanguages3::class,
        Experience::class,Experience2::class,Experience3::class,
        JobRequirements::class, Achievements::class, LangLocale::class],
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CvDatabase : RoomDatabase() {

    abstract fun getPersonalInfoDao(): PersonalInfoDao
    abstract fun getContactInfoDao(): ContactInfoDao
    abstract fun getEducationDao(): EducationDao
    abstract fun getLangDao(): LanguagesDao
    abstract fun getExperienceDao(): ExperienceDao
    abstract fun getJobReqDao(): JobReqDao

    companion object {
        @Volatile
        private var instance: CvDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CvDatabase::class.java,
            "resume_database"
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}
