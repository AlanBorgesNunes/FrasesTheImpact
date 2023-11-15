package com.app.frasestheimpact.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.frasestheimpact.repository.ApiRepository
import com.app.frasestheimpact.utlis.DadosApi
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


    private val _postFrases = MutableLiveData<UiState<DadosApi>>()
    val postFrases : LiveData<UiState<DadosApi>>
        get() = _postFrases

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

}