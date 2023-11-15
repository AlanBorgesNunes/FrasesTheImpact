package com.app.frasestheimpact.di

import com.app.frasestheimpact.utlis.ApiConstantes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit{
        if (retrofit == null){
         retrofit = Retrofit.Builder()
             .baseUrl(ApiConstantes.BASE_URL)
             .addConverterFactory(GsonConverterFactory.create())
             .build()
        }

        return retrofit!!
    }

}