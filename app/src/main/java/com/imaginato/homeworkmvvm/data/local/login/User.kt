package com.imaginato.homeworkmvvm.data.local.login

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User constructor(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") var name: String?=null,
    @ColumnInfo(name = "xAcc") var xAcc: String?=null,
    @ColumnInfo(name = "isDeleted") var isDeleted: Boolean?=null
)