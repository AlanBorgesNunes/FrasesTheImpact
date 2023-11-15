package com.app.frasestheimpact.repository

import com.app.frasestheimpact.utlis.ApiService
import com.app.frasestheimpact.utlis.DadosApi
import com.app.frasestheimpact.utlis.DadosDaFrase
import com.app.frasestheimpact.utlis.DadosPostApi
import com.muita.megasorte.utils.UiState
import retrofit2.Retrofit

class ApiRepositoryImpl(
  private val retrofit: Retrofit
) : ApiRepository{

    val apiService = retrofit.create(ApiService::class.java)

    override suspend fun getFrases(): UiState<DadosApi> {
        try {
            val response = apiService.getFrases()
            if (response.isSuccessful){
                val data  = response.body() as DadosApi
                return UiState.Success(data)
            }else{
                return UiState.Failure("Erro na chamada a api!")
            }


        }catch (e: Exception){
            return UiState.Failure(e.localizedMessage)
        }
    }

    override suspend fun postFrases(result: DadosPostApi): UiState<DadosApi> {
        try {
            val response = apiService.postFrase(result)
            if (response.isSuccessful){
                val dados = response.body() as DadosApi
                return UiState.Success(dados)
            }else{
                return UiState.Failure("Erro ao postar frase!")
            }

        }catch (e: Exception){
            return UiState.Failure(e.localizedMessage)
        }
    }
}