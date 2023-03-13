package com.yusufyildiz.experttalk.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.yusufyildiz.experttalk.common.Utils.BASE_URL
import com.yusufyildiz.experttalk.data.source.remote.AuthApi
import com.yusufyildiz.experttalk.domain.repository.auth.ExpertRepository
import com.yusufyildiz.experttalk.data.repository.auth.expert.ExpertRepositoryImpl
import com.yusufyildiz.experttalk.domain.source.message.ChatSocketService
import com.yusufyildiz.experttalk.data.source.remote.ChatSocketServiceImpl
import com.yusufyildiz.experttalk.domain.source.message.MessageService
import com.yusufyildiz.experttalk.data.source.remote.MessageServiceImpl
import com.yusufyildiz.experttalk.domain.repository.auth.UserRepository
import com.yusufyildiz.experttalk.data.repository.auth.user.UserRepositoryImpl
import com.yusufyildiz.experttalk.domain.repository.video.VideoSdkEngineRepository
import com.yusufyildiz.experttalk.data.repository.video.VideoSdkEngineRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.features.websocket.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthApi(): AuthApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
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

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO){
            install(Logging)
            install(WebSockets)
            install(JsonFeature){
                serializer = KotlinxSerializer()
            }
        }
    }

    @Provides
    @Singleton
    fun provideMessageService(client: HttpClient): MessageService {
        return MessageServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideChatSocketService(client: HttpClient): ChatSocketService {
        return ChatSocketServiceImpl(client)
    }

}