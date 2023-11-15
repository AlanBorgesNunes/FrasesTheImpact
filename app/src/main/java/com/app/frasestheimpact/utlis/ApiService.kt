package com.app.frasestheimpact.utlis

import com.muita.megasorte.utils.UiState
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("obter")
    suspend fun getFrases() : Response<DadosApi>

    @POST("criar")
    suspend fun postFrase(@Body result: DadosPostApi) : Response<DadosApi>

}