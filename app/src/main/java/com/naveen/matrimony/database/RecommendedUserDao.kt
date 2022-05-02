package com.naveen.matrimony.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.naveen.matrimony.model.RecommendedUser

@Dao
interface RecommendedUserDao {
    @Query("SELECT * FROM recommended")
    fun getAll(): LiveData<List<RecommendedUser>>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getByName(id: String): List<RecommendedUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: ArrayList<RecommendedUser>)

    @Delete
    fun delete(user: RecommendedUser)

    @Update
    fun update(user: RecommendedUser)


    @Delete
    fun deleteAll(videoData: ArrayList<RecommendedUser>)

}