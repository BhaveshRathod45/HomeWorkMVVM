package com.imaginato.homeworkmvvm.data.remote.login

import com.imaginato.homeworkmvvm.data.local.login.User
import com.imaginato.homeworkmvvm.data.local.login.UserDao
import com.imaginato.homeworkmvvm.data.remote.Result
import com.imaginato.homeworkmvvm.data.remote.login.request.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class LoginDataRepository constructor(
    private var api: LoginApi,
    private val userDao: UserDao,
) : LoginRepository {
    override suspend fun doLogin(loginRequest: LoginRequest) = flow {

        api.doLogin(loginRequest).apply {
            val data = body()?.data
            if (isSuccessful && data != null) {
                if (body()?.errorCode == "00") {
                    val user = data.userId?.let {
                        User(
                            data.userId,
                            data.userName,
                            headers()["X-Acc"],
                            data.isDeleted
                        )
                    }
                    emit(Result.Success(user))
                } else {
                    emit(Result.Error(body()?.errorMessage))
                }
            } else {
                emit(Result.Error("Some thing went wrong"))
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }
}