package com.app.frasestheimpact.repository

import android.app.Activity
import android.content.Context
import com.app.frasestheimpact.utlis.DadosApi
import com.app.frasestheimpact.utlis.DadosDaFrase
import com.app.frasestheimpact.utlis.DadosPostApi
import com.muita.megasorte.utils.UiState
import retrofit2.Response

interface ApiRepository {

    suspend fun getFrases() : UiState<DadosApi>

    suspend fun postFrases(result: DadosPostApi): UiState<String>


    //Admob
    suspend fun loadInterstitialAd(context: Context, result:(UiState<String>) -> Unit)
    suspend fun showInterstitialAd(activity: Activity, result: (UiState<String>) -> Unit)
}