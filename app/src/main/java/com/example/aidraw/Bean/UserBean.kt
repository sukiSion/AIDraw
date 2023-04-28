package com.example.aidraw.Bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class UserBean(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "password")val password: String,
    @ColumnInfo(name = "phone") val phone: String,
    @ColumnInfo(name = "email") val email: String,
)