package com.naveen.matrimony.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.naveen.matrimony.model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE id = :id")
    fun getByName(id: String): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: ArrayList<User>)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)


    @Delete
    fun deleteAll(videoData: ArrayList<User>)

}