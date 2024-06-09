package com.app.frasestheimpact.viewModels

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.frasestheimpact.repository.ApiRepository
import com.app.frasestheimpact.utlis.DadosApi
import com.app.frasestheimpact.utlis.DadosDaFrase
import com.app.frasestheimpact.utlis.DadosPostApi
import com.muita.megasorte.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelApi @Inject constructor(
    private val repository: ApiRepository
) : ViewModel() {

    private val _resultFrases = MutableLiveData<UiState<DadosApi>>()
    val resultFrases : LiveData<UiState<DadosApi>>
        get() = _resultFrases


    private val _postFrases = MutableLiveData<UiState<String>>()
    val postFrases : LiveData<UiState<String>>
        get() = _postFrases



    private val _adInter = MutableLiveData<UiState<String>>()
    val adInter: LiveData<UiState<String>>
        get() = _adInter

    private val _adShowInter = MutableLiveData<UiState<String>>()
    val adShowInter: LiveData<UiState<String>>
        get() = _adShowInter

    fun getFrases(){
        viewModelScope.launch {
            _resultFrases.value = UiState.Loading

            val resultData = repository.getFrases()

            when(resultData){
                is UiState.Failure ->{
                    _resultFrases.value = UiState.Failure(
                        resultData.error
                    )
                }
                UiState.Loading -> {}
                is UiState.Success -> {
                    _resultFrases.value = UiState.Success(
                        resultData.data
                    )
                }
            }

        }
    }

    fun postFrase(result: DadosPostApi){
        viewModelScope.launch {
            _postFrases.value = UiState.Loading

            val resultado = repository.postFrases(result)
            when(resultado){
                is UiState.Failure -> {
                    _postFrases.value = UiState.Failure(
                        resultado.error
                    )
                }
                UiState.Loading -> {}
                is UiState.Success -> {
                    _postFrases.value = UiState.Success(
                        resultado.data
                    )
                }
            }

        }
    }

    fun inter(context: Context){
        viewModelScope.launch {
            _adInter.value = UiState.Loading
            repository.loadInterstitialAd(context){
                _adInter.value = it
            }
        }
    }

    fun interShow(activity: Activity){
        viewModelScope.launch {
            _adShowInter.value = UiState.Loading
            repository.showInterstitialAd(activity){
                _adShowInter.value = it
            }
        }
    }

}