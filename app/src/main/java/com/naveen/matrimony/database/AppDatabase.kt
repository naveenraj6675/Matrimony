package com.naveen.matrimony.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.gotech.youtube.database.typeconverters.UserTypeConverter
import com.naveen.matrimony.model.RecommendedUser
import com.naveen.matrimony.model.User


@Database(entities = [User::class, RecommendedUser::class], version = 1)
@TypeConverters(UserTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDataDao(): UserDao
    abstract fun recommendedUserDataDao(): RecommendedUserDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private const val DB_NAME = "matrimony.db"
        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, DB_NAME
                        )
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    Log.d("AppDatabase", "create db called")
                                }
                            })
                            .build()
                    }
                }
            }
            return INSTANCE
        }
    }

}