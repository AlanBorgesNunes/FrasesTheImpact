package com.app.frasestheimpact.di

import com.app.frasestheimpact.repository.ApiRepository
import com.app.frasestheimpact.repository.ApiRepositoryImpl
import com.google.android.gms.ads.AdRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideApiRepository(
        retrofit: Retrofit,
        adRequest: AdRequest
    ): ApiRepository{
        return ApiRepositoryImpl(retrofit, adRequest)
    }

}