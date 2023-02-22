package com.yusufyildiz.experttalk.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.yusufyildiz.experttalk.data.auth.AuthApi
import com.yusufyildiz.experttalk.data.auth.expert.ExpertRepository
import com.yusufyildiz.experttalk.data.auth.expert.ExpertRepositoryImpl
import com.yusufyildiz.experttalk.data.auth.user.UserRepository
import com.yusufyildiz.experttalk.data.auth.user.UserRepositoryImpl
import com.yusufyildiz.experttalk.data.video.VideoSdkEngineRepository
import com.yusufyildiz.experttalk.data.video.VideoSdkEngineRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.182:8080/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideSharedPref(app: Application): SharedPreferences{
        return app.getSharedPreferences("prefs",MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApi, prefs: SharedPreferences): UserRepository {
        return UserRepositoryImpl(api,prefs)
    }

    @Provides
    @Singleton
    fun provideExpertRepository(api: AuthApi, prefs: SharedPreferences): ExpertRepository {
        return ExpertRepositoryImpl(api,prefs)
    }

    @Provides
    @Singleton
    fun provideVideoSdkEngineRepository(): VideoSdkEngineRepository {
        return VideoSdkEngineRepositoryImpl()
    }


}