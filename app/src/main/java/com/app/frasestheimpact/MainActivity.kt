package com.app.frasestheimpact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.app.frasestheimpact.databinding.ActivityMainBinding
import com.app.frasestheimpact.utlis.DadosPostApi
import com.app.frasestheimpact.utlis.toast
import com.app.frasestheimpact.viewModels.ViewModelApi
import com.muita.megasorte.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModelApi: ViewModelApi by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelApi.getFrases()
        observers()

    }

    private fun postFrase() {
        viewModelApi.postFrase(
            DadosPostApi(
                frase = "O talento é ultrapassado pelo esforço sempre que o talento não se esforca!",
                nomeAutor = "Allan Sparks"
            )
        )
    }

    private fun observers(){
        viewModelApi.resultFrases.observe(this){
            when(it){
                is UiState.Failure -> {
                    toast(it.error)
                }
                UiState.Loading ->{

                }
                is UiState.Success ->{
                    if (it.data.status == "OK"){
                        binding.resposta.text = it.data.resposta?.get(3)?.frase.toString()
                    }
                }
            }
        }

        viewModelApi.postFrases.observe(this){
            when(it){
                is UiState.Failure -> {toast(it.error)}
                UiState.Loading -> {}
                is UiState.Success -> {
                    if (it.data.status == "Ok"){
                        toast(it.data.statusMensagem)
                    }
                }
            }
        }
    }
}