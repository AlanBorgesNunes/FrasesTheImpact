package com.app.frasestheimpact.repository

import com.app.frasestheimpact.utlis.DadosApi
import com.app.frasestheimpact.utlis.DadosPostApi
import com.muita.megasorte.utils.UiState
import retrofit2.Response

interface ApiRepository {

    suspend fun getFrases() : UiState<DadosApi>

    suspend fun postFrases(result: DadosPostApi): UiState<DadosApi>
}