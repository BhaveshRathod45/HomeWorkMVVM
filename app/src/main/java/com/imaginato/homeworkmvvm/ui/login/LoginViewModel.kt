package com.imaginato.homeworkmvvm.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.imaginato.homeworkmvvm.data.local.login.User
import com.imaginato.homeworkmvvm.data.remote.Result
import com.imaginato.homeworkmvvm.data.remote.login.LoginRepository
import com.imaginato.homeworkmvvm.data.remote.login.request.LoginRequest
import com.imaginato.homeworkmvvm.ui.base.BaseViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.inject

@KoinApiExtension
class LoginViewModel(private val repository: LoginRepository) : BaseViewModel() {
    private var _userLiveData: MutableLiveData<User> = MutableLiveData()
    val userLiveData: LiveData<User>
        get() {
            return _userLiveData
        }

    /**
     * Checking email and password validation
     * if valid call api and handle success and failed response
     */
    fun doLogin(userName: String, password: String) {
        if (userName.isEmpty()) {
            _errorMessage.value = "Username cannot be empty"
            return
        } else if (password.isEmpty()) {
            _errorMessage.value = "Password cannot be empty"
            return
        } else if (password.length < 6) {
            _errorMessage.value = "Password must be great then 6 character"
            return
        } else {

            viewModelScope.launch {
                val loginRequest = LoginRequest(userName, password)
                repository.doLogin(loginRequest)
                    .onStart {
                        _progress.value = true
                    }.catch {
                        _errorMessage.value = it.message
                        _progress.value = false
                    }.collect { result ->
                        when (result) {
                            is Result.Error -> {
                                _errorMessage.value = result.message
                                _progress.value = false
                            }
                            is Result.Loading -> {
                                _progress.value = true
                            }
                            is Result.Success -> {
                                _progress.value = false

                                _userLiveData.value = result.data
                                if (result.data != null) {

                                    repository.insertUser(result.data)
                                }
                            }
                        }
                    }
            }
        }
    }


}