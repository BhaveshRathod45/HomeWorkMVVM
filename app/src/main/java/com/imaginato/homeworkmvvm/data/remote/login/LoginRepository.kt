package com.imaginato.homeworkmvvm.data.remote.login

import com.imaginato.homeworkmvvm.data.local.login.User
import com.imaginato.homeworkmvvm.data.remote.Result
import com.imaginato.homeworkmvvm.data.remote.login.request.LoginRequest
import com.imaginato.homeworkmvvm.data.remote.login.response.LoginResponse
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun doLogin(loginRequest: LoginRequest): Flow<Result<User>>
    suspend fun insertUser(user: User)
}