package com.rana.hamza.tastyrecipes.di

import com.rana.hamza.tastyrecipes.data.network.FoodRecipesApi
import com.rana.hamza.tastyrecipes.util.Constants.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideFoodRecipeApi(retrofit: Retrofit): FoodRecipesApi =
        retrofit.create(FoodRecipesApi::class.java)

    @Singleton
    @Provides
     fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()

    @Singleton
    @Provides
     fun providesGson(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
     fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS)
        .build()
}