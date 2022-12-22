package com.imaginato.homeworkmvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.imaginato.homeworkmvvm.data.local.login.UserDao
import com.imaginato.homeworkmvvm.data.remote.login.LoginApi
import com.imaginato.homeworkmvvm.data.remote.login.LoginDataRepository
import com.imaginato.homeworkmvvm.data.remote.login.response.LoginResponse
import com.imaginato.homeworkmvvm.ui.login.LoginViewModel
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.koin.core.component.KoinApiExtension
import org.mockito.kotlin.mock
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@OptIn(KoinApiExtension::class)
class LoginViewModelUnitTest {

    private lateinit var viewModel: LoginViewModel
    private var loginApi: LoginApi = mock {}
    private var userDao: UserDao = mock {}
    private lateinit var repository: LoginDataRepository
    //private var loginResponse: retrofit2.Call<LoginResponse?> = mock {}

    @Before
    fun setInit() {
        repository = LoginDataRepository(loginApi,userDao)
        viewModel = LoginViewModel()
    }

    @Test
    fun username_isEmpty() {
        viewModel.doLogin("","")
        assert(viewModel.errorMessage.value == "Username cannot be empty")
    }

    @Test
    fun usernameisNotEmpty() {
        viewModel.doLogin("123","")
        assert(viewModel.errorMessage.awaitValue() != "Username cannot be empty")
    }

    @Test
    fun `password is empty`() {
        viewModel.doLogin("1234","")
        assert(viewModel.errorMessage.awaitValue() == "Password cannot be empty")
    }

    @Test
    fun `password is not empty`() {
        viewModel.doLogin("1234","123")
        assert(viewModel.errorMessage.awaitValue() != "Password cannot be empty")
    }

    @Test
    fun isPasswordLengthLessThen6() {
        viewModel.doLogin("1234","123")
        assert(viewModel.errorMessage.awaitValue() == "Password must be great then 6 character")
    }

    @Test
    fun isPasswordLengthNotLessThen6() {
        viewModel.doLogin("1234","123574")
        assert(viewModel.errorMessage.awaitValue() != "Password must be great then 6 character")
    }


}

fun <T> LiveData<T>.awaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@awaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}


