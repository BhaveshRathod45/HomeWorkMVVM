package com.imaginato.homeworkmvvm.data.remote.login.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("userId")
    val userId: String? = null,
    @SerializedName("userName")
    val userName: String? = null,
    @SerializedName("isDeleted")
    val isDeleted: Boolean? = false,
)