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
    version = 2,
    entities = [PersonalInfo::class, ContactInfo::class, Social::class,
        Education::class,Education2::class,Education3::class,
        MotherLanguage::class,
        OtherLanguages::class,OtherLanguages2::class,OtherLanguages3::class,
        Experience::class,Experience2::class,Experience3::class,
        JobRequirements::class, Achievements::class],
    exportSchema = false

)
@TypeConverters(Converters::class)
abstract class CvDatabase : RoomDatabase() {

    abstract fun getCvDao(): CvDao
    abstract fun getPersonalInfoDao(): PersonalInfoDao
    abstract fun getContactInfoDao(): ContactInfoDao
    abstract fun getEducationDao(): EducationDao
    abstract fun getLangDao(): LanguagesDao
    abstract fun getExperienceDao(): ExperienceDao
    abstract fun getJobReqDao(): JobReqDao

    companion object {

//        private val migration_2_3: Migration = object : Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE personal_info_table ADD COLUMN firstname TEXT DEFAULT ''")
//                database.execSQL("ALTER TABLE personal_info_table ADD COLUMN lastname TEXT DEFAULT ''")
//                database.execSQL("ALTER TABLE personal_info_table ADD COLUMN middlename TEXT DEFAULT ''")
//                database.execSQL("ALTER TABLE personal_info_table ADD COLUMN birthdate TEXT DEFAULT ''")
//                database.execSQL("ALTER TABLE personal_info_table ADD COLUMN citizenship TEXT DEFAULT ''")
//                database.execSQL("ALTER TABLE personal_info_table ADD COLUMN marital_status TEXT DEFAULT ''")
//                database.execSQL("ALTER TABLE personal_info_table ADD COLUMN gender TEXT DEFAULT ''")
//            }
//        }


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

//        fun getAppDatabase(context: Context): CvDatabase? {
//
//            if (INSTANCE == null) {
//
//                INSTANCE = Room.databaseBuilder<CvDatabase>(
//                    context.applicationContext, CvDatabase::class.java, "cv_database"
//                )
//                    .addMigrations(migration_1_2)
//                    .allowMainThreadQueries()
//                    .build()
//
//            }
//            return INSTANCE
//        }
//    }
}
