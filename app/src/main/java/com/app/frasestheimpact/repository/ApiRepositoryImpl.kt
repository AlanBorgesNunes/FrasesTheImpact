package com.app.frasestheimpact.repository

import android.app.Activity
import android.content.Context
import android.util.Log
import com.app.frasestheimpact.utlis.ApiService
import com.app.frasestheimpact.utlis.DadosApi
import com.app.frasestheimpact.utlis.DadosDaFrase
import com.app.frasestheimpact.utlis.DadosPostApi
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.muita.megasorte.utils.UiState
import retrofit2.Retrofit

class ApiRepositoryImpl(
    private var retrofit: Retrofit,
    private var adRequest: AdRequest

) : ApiRepository{

    private val apiService = retrofit.create(ApiService::class.java)
    private var interstitialAd: InterstitialAd? = null


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

    override suspend fun postFrases(result: DadosPostApi): UiState<String> {
        return try {
            val response = apiService.postFrase(result)
            if (response.isSuccessful){
                UiState.Success(
                    "Sucess ao postar frase!"
                )
            }else{
                UiState.Failure(
                    "Erro ao postar frase!")
            }

        }catch (e: Exception){
            Log.d("GARIBALDO", "postFrases: ${e.localizedMessage}")
            UiState.Failure(e.localizedMessage)
        }
    }

    override suspend fun loadInterstitialAd(context: Context, result: (UiState<String>) -> Unit) {
        InterstitialAd.load(context,"ca-app-pub-6827886217820908/4314261202",
            adRequest,object  : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    result.invoke(UiState.Failure(
                        "Null"
                    ))
                    interstitialAd = null
                }

                override fun onAdLoaded(MinterstitialAd: InterstitialAd) {
                    result.invoke(UiState.Success(
                        "Sucess"
                    ))
                    interstitialAd = MinterstitialAd
                }
            })
    }

    override suspend fun showInterstitialAd(activity: Activity, result: (UiState<String>) -> Unit) {
        if (interstitialAd != null){
            interstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() {
                    result.invoke(UiState.Success("den"))
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                    result.invoke(UiState.Success("failed"))
                }
            }

            interstitialAd?.show(activity).run {
                result.invoke(UiState.Success(
                    "show inter"
                ))
            }
        }else{
            result.invoke(UiState.Failure(
                "innter NUll"
            ))
        }
    }

}