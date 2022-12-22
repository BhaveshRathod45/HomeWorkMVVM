package com.imaginato.homeworkmvvm.data.remote


data class ApiResponse<out T>(
    val errorCode: String? = null,
    val data: T? = null,
    val errorMessage: String? = null,
)


