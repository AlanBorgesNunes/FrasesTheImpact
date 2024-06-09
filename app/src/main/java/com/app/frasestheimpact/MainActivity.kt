package com.app.frasestheimpact

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.frasestheimpact.adapters.AdapterFrases
import com.app.frasestheimpact.databinding.ActivityMainBinding
import com.app.frasestheimpact.utlis.DadosDaFrase
import com.app.frasestheimpact.utlis.DadosPostApi
import com.app.frasestheimpact.utlis.hide
import com.app.frasestheimpact.utlis.show
import com.app.frasestheimpact.utlis.toast
import com.app.frasestheimpact.viewModels.ViewModelApi
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.muita.megasorte.utils.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AdapterFrases.ClickShare {
    private lateinit var binding: ActivityMainBinding

    private var list: ArrayList<DadosDaFrase> = arrayListOf<DadosDaFrase>()

    private val viewModelApi: ViewModelApi by viewModels()

    private val adapter: AdapterFrases by lazy {
        AdapterFrases(this, list, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)


        viewModelApi.inter(this)
        observers()
        obserrverGetFrases()
        viewModelApi.getFrases()
        binding.resposta.setOnClickListener {
            if (binding.frase.text.toString().isBlank() ||
                binding.autor.text.toString().isBlank()){
                toast("Textos obrigatorrios em branco!!")
            }else {
                viewModelApi.postFrase(
                    DadosPostApi(
                        frase = binding.frase.text.toString(),
                        nomeAutor = binding.autor.text.toString()
                    )
                )
            }
        }

    }

    private fun observers(){

        viewModelApi.postFrases.observe(this){
            when(it){
                is UiState.Failure -> {
                    binding.progress.hide()
                    binding.frase.setText("")
                    binding.autor.setText("")
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed(Runnable {
                        obserrverGetFrases()
                    }, 1500)
                    toast("Sucees ao postar!")
                }
                UiState.Loading -> {
                    binding.progress.show()

                }
                is UiState.Success -> {
                    binding.progress.hide()
                    binding.frase.setText("")
                    binding.autor.setText("")
                    obserrverGetFrases()
                    viewModelApi.interShow(this)
                   toast(it.data)
                }
            }
        }
    }
    private fun obserrverGetFrases(){
        viewModelApi.resultFrases.observe(this){
            when(it){
                is UiState.Failure -> {
                    toast(it.error)
                    Log.d("GARIBALDO", "observers: ${it.error} ")
                }
                UiState.Loading ->{
                    list.clear()
                    binding.llFrasesFalsas.show()
                }
                is UiState.Success ->{
                    list.clear()
                    val handler = Handler(Looper.getMainLooper())

                    handler.postDelayed(Runnable {
                        binding.llFrasesFalsas.hide()
                        binding.rvMain.show()
                    },3000)

                    if (it.data.status == "OK"){
                        list.addAll(it.data.resposta!!)
                        adapter.updateList(list)
                        binding.rvMain.layoutManager = LinearLayoutManager(this)
                        binding.rvMain.adapter = adapter
                    }
                }
            }
        }
    }
    override fun clickShare(daFrase: DadosDaFrase) {
        val intent = Intent(this@MainActivity, EditActivity::class.java)
        intent.putExtra("FRASE", daFrase.frase)
        intent.putExtra("AUTOR", daFrase.nomeAutor)
        startActivity(intent)
    }
}