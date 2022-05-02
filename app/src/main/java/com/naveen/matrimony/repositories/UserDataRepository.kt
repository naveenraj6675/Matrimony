package com.naveen.matrimony.repositories

import android.app.Application
import com.naveen.matrimony.database.AppDatabase
import com.naveen.matrimony.database.RecommendedUserDao
import com.naveen.matrimony.database.UserDao
import com.naveen.matrimony.model.RecommendedUser
import com.naveen.matrimony.model.User


class UserDataRepository(private val application: Application) {

    companion object {
        private var INSTANCE: UserDataRepository? = null
        fun getInstance(application: Application): UserDataRepository {
            return if (INSTANCE != null) {
                INSTANCE!!
            } else {
                INSTANCE = UserDataRepository(application)
                INSTANCE!!
            }
        }
    }

    private val userDataDao: UserDao by lazy {
        AppDatabase.getDatabase(application)!!.userDataDao()
    }

    private val recommendedDataDao: RecommendedUserDao by lazy {
        AppDatabase.getDatabase(application)!!.recommendedUserDataDao()
    }

    val usersLiveData = userDataDao.getAll()

    val recommendedLiveData = recommendedDataDao.getAll()


    fun addUser(list: ArrayList<User>) {
        userDataDao.insert(list)
    }

    fun deleteUser(user: User) {
        userDataDao.delete(user)
    }


    fun updateVideo(user: User) {
        userDataDao.update(user)
    }

    fun deleteAll(list: ArrayList<User>) {
        userDataDao.deleteAll(list)

    }

    fun addRecommendedUser(list: ArrayList<RecommendedUser>) {
        recommendedDataDao.insert(list)
    }

    fun deleteRecommendedUser(user: RecommendedUser) {
        recommendedDataDao.delete(user)
    }


    fun updateRecommendedUser(user: RecommendedUser) {
        recommendedDataDao.update(user)
    }

    fun deleteRecommendedAll(list: ArrayList<RecommendedUser>) {
        recommendedDataDao.deleteAll(list)
    }


}

