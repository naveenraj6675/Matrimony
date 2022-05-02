package com.naveen.matrimony.viewmodel

import android.app.Application
import com.naveen.matrimony.model.RecommendedUser
import com.naveen.matrimony.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : MyBaseViewModel(application) {

    val localUserLiveData = userDataRepository.usersLiveData
    val recommendedUserLiveData = userDataRepository.recommendedLiveData

    fun saveUsers(list: ArrayList<User>) {

        CoroutineScope(exceptionHandler).launch {
            userDataRepository.addUser(list)
        }

    }

    fun updateUser(item: User, isDelete: Boolean) {
        if (isDelete) {
            CoroutineScope(exceptionHandler).launch {
                userDataRepository.deleteUser(item)
            }
        } else {
            CoroutineScope(exceptionHandler).launch {
                userDataRepository.updateVideo(item)
            }
        }
    }

    fun addRecommendedUser(list: ArrayList<RecommendedUser>) {

        CoroutineScope(exceptionHandler).launch {
            userDataRepository.addRecommendedUser(list)
        }

    }

    fun updateRecommendedUser(item: RecommendedUser, isDelete: Boolean) {
        if (isDelete) {
            CoroutineScope(exceptionHandler).launch {
                userDataRepository.deleteRecommendedUser(item)
            }
        } else {
            CoroutineScope(exceptionHandler).launch {
                userDataRepository.updateRecommendedUser(item)
            }
        }
    }


    fun deleteAll(list: ArrayList<User>) {
        CoroutineScope(exceptionHandler).launch {
            userDataRepository.deleteAll(list)
        }
    }


}