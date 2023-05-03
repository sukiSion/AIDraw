package com.example.aidraw.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.aidraw.Bean.UserBean
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun addUser(user: UserBean): Long

    @Query("select * from User where name == :name and password == :password")
   fun queryUser(name: String , password: String) : Flow<List<UserBean>>
}