@file:OptIn(KoinApiExtension::class)
package com.imaginato.homeworkmvvm.domain

import android.app.Application
import androidx.room.Room
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.imaginato.homeworkmvvm.data.local.login.UserDatabase
import com.imaginato.homeworkmvvm.data.local.login.UserDao
import com.imaginato.homeworkmvvm.data.remote.login.LoginApi
import com.imaginato.homeworkmvvm.data.remote.login.LoginDataRepository
import com.imaginato.homeworkmvvm.data.remote.login.LoginRepository
import com.imaginato.homeworkmvvm.ui.login.LoginViewModel
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://private-222d3-homework5.apiary-mock.com/api/"

val databaseModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideUserDao(get()) }
}

val netModules = module {
    single { provideInterceptors() }
    single { provideOkHttpClient(get()) }
    single { provideRetrofit(get()) }
    single { provideGson() }
}

val apiModules = module {
    single { provideLoginApi(get()) }
}

val repositoryModules = module {
    single { provideLoginRepo(get(),get()) }
}

val viewModelModules = module {
    viewModel {
        LoginViewModel()
    }
}

private fun provideLoginRepo(api: LoginApi,userDao: UserDao): LoginRepository {
    return LoginDataRepository(api,userDao)
}

private fun provideLoginApi(retrofit: Retrofit): LoginApi = retrofit.create(LoginApi::class.java)

private fun provideDatabase(application: Application): UserDatabase {
    return Room.databaseBuilder(application, UserDatabase::class.java, "UserDatabase")
        .fallbackToDestructiveMigration()
        .build()
}

private fun provideUserDao(database: UserDatabase): UserDao {
    return database.userDao
}

private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

private fun provideOkHttpClient(interceptors: ArrayList<Interceptor>): OkHttpClient {
    val clientBuilder = OkHttpClient.Builder()
    clientBuilder.readTimeout(2, TimeUnit.MINUTES)
    clientBuilder.connectTimeout(2, TimeUnit.MINUTES)
    interceptors.forEach { clientBuilder.addInterceptor(it) }
    return clientBuilder.build()
}

private fun provideInterceptors(): ArrayList<Interceptor> {
    val interceptors = arrayListOf<Interceptor>()
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    interceptors.add(loggingInterceptor)
    return interceptors
}

fun provideGson(): Gson {
    return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
}
