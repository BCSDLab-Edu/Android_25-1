package com.example.bcsd_android_2025_1.app

import com.example.bcsd_android_2025_1.data.GithubApi
import com.example.bcsd_android_2025_1.domain.GithubRepository
import com.example.bcsd_android_2025_1.domain.GithubRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideGitHubApi(): GithubApi {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GithubApi::class.java)
    }

    @Provides
    fun provideGitHubRepository(api: GithubApi): GithubRepository{
        return GithubRepositoryImpl(api)
    }
}