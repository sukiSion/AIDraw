package com.example.aidraw.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.aidraw.Bean.UserBean

@Database(entities = [UserBean::class] , version = 1)
abstract class UserDataBase :RoomDatabase() {
    abstract fun userDao() : UserDao

    companion object{
        @Volatile
        private var INSTANCE: UserDataBase? = null

        fun getDatabase(context: Context): UserDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    UserDataBase::class.java,
                    "User.db"
                )
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}