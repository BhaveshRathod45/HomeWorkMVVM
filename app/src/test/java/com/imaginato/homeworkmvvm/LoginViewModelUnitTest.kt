package com.imaginato.homeworkmvvm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.imaginato.homeworkmvvm.data.local.login.User
import com.imaginato.homeworkmvvm.data.local.login.UserDao
import com.imaginato.homeworkmvvm.data.remote.Result
import com.imaginato.homeworkmvvm.data.remote.login.LoginApi
import com.imaginato.homeworkmvvm.data.remote.login.LoginDataRepository
import com.imaginato.homeworkmvvm.data.remote.login.response.LoginResponse
import com.imaginato.homeworkmvvm.ui.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.component.KoinApiExtension
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Callback
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@OptIn(KoinApiExtension::class)
class LoginViewModelUnitTest {

    private lateinit var viewModel: LoginViewModel
    private var loginApi: LoginApi = mock {}
    private var userDao: UserDao = mock {}
    private lateinit var repository: LoginDataRepository
    private var loginResponse: retrofit2.Call<LoginResponse?> = mock {}

    val dispatcher = StandardTestDispatcher()

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setInit() {
        Dispatchers.setMain(dispatcher)
        repository = LoginDataRepository(loginApi, userDao)
        viewModel = LoginViewModel(repository)

    }


    @Test
    fun usernameIsEmpty() {
        viewModel.doLogin("", "")
        assert(viewModel.errorMessage.value == "Username cannot be empty")
    }

    @Test
    fun userNameIsNotEmpty() {
        viewModel.doLogin("123", "")
        assert(viewModel.errorMessage.awaitValue() != "Username cannot be empty")
    }

    @Test
    fun passwordIsEmpty() {
        viewModel.doLogin("1234", "")
        assert(viewModel.errorMessage.awaitValue() == "Password cannot be empty")
    }

    @Test
    fun passwordIsNotEmpty() {
        viewModel.doLogin("1234", "123")
        assert(viewModel.errorMessage.awaitValue() != "Password cannot be empty")
    }

    @Test
    fun isPasswordLengthLessThen6() {
        viewModel.doLogin("1234", "123")
        assert(viewModel.errorMessage.awaitValue() == "Password must be great then 6 character")
    }

    @Test
    fun isPasswordLengthNotLessThen6() {
        viewModel.doLogin("1234", "123574")
        assert(viewModel.errorMessage.awaitValue() != "Password must be great then 6 character")
    }


   /* @Test
    fun `login with valid username should set loginResult as success`() = runTest {
        val flow = MutableSharedFlow<Result<User>>(replay = 1)
        whenever(repository.doLogin(any())).doReturn(flow)

        launch {
            flow.emit(Result.Success(User("", "")))
        }.join()

        launch {
            viewModel.doLogin("tes", "eedjfdjfdjf")
        }.join()

        assert(viewModel.userLiveData.awaitValue() != null)
    }

    @Test
    fun `doLogin viewModel method test with error`() {
        Mockito.doAnswer {
            val callback: Callback<LoginResponse?> = it?.getArgument(
                0
            )!!
            callback.onFailure(loginResponse, Exception("Error"))

        }.`when`(loginResponse).enqueue(any())

        // whenever(loginApi.doLogin(any())).doReturn(loginResponse)
        viewModel.doLogin("", "")

        //assert(viewModel.mError.getOrAwaitValue() == "Error")
    }
*/

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


