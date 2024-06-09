package com.app.frasestheimpact

import android.R
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import com.app.frasestheimpact.databinding.ActivityEditBinding
import com.app.frasestheimpact.utlis.hide
import com.app.frasestheimpact.utlis.show
import com.app.frasestheimpact.utlis.toast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.skydoves.colorpickerview.AlphaTileView
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode
import com.skydoves.colorpickerview.flag.FlagView
import com.skydoves.colorpickerview.listeners.ColorListener


class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding

    private var frase: String? = null
    private var autor: String? = null

    private  var imagemOriginal : Bitmap? = null

    private val paint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        binding.adView2.loadAd(adRequest)

        frase = intent.getStringExtra("FRASE")
        autor = intent.getStringExtra("AUTOR")

        binding.btnCompartilhar.isEnabled = false

        getFrase()

        binding.btnBuscar.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1)
        }

        binding.shareFrase.setOnClickListener {
            compartilharTexto(frase.toString() + "\n -$autor")
        }

        val snakBAr = binding.moreTxt
        snakBAr.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val fatorAjuste = progress.toFloat() / 10.0f

                atualizarImagemComFrase(fatorAjuste, paint.color)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        binding.chamaCor.setOnClickListener {
            showItemPcture()
        }

    }
    private fun compartilharTexto(texto: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, texto)

        val chooser = Intent.createChooser(intent, "Compartilhar via")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooser)
        }
    }
    private fun showItemPcture(){
        binding.moreCor.show()
        binding.moreCor.setColorListener(object : ColorListener{
            override fun onColorSelected(color: Int, fromUser: Boolean) {
                if (fromUser) {
                    paint.color = color
                    atualizarImagemComFrase(obterFatorAjuste(), paint.color)
                }else{
                    toast("não ta dando certo mané")
                }
            }

        })
    }
    private fun obterFatorAjuste(): Float {
        return binding.moreTxt.progress.toFloat() / 10.0f
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            binding.escolhaImagem.hide()
            binding.imageReceive.show()
            binding.llEdite.show()
            binding.btnCompartilhar.isEnabled = true

            val imagemUri = data.data
            val frase = intent.getStringExtra("FRASE")

            // Carregar a imagem original
            imagemOriginal = MediaStore.Images.Media.getBitmap(contentResolver, imagemUri)

            var fatorFloat = 0.1f



            // Adicionar a frase na imagem
            val imagemComFrase = addTextInImage(imagemOriginal!!, frase.toString() + "-$autor",fatorFloat, paint.color)

            atualizarImagemComFrase(fatorFloat, paint.color)

            // Exibir a imagem resultante
            exibirImagem(imagemComFrase)



        }
    }
    private fun exibirImagem(imagem: Bitmap){
        val imageView = binding.imageReceive
        imageView.setImageBitmap(imagem)
    }
    private fun atualizarImagemComFrase(fatorAjuste: Float, fontColor: Int) {
        val imagemComFrase = addTextInImage(imagemOriginal!!, frase.toString() + "-$autor", fatorAjuste, fontColor)
        binding.btnCompartilhar.setOnClickListener {
            compartilharImagem(imagemComFrase)
        }
        // Exibir a imagem resultante em uma ImageView ou conforme necessário
        binding.imageReceive.setImageBitmap(imagemComFrase)
    }
    private fun compartilharImagem(imagem: Bitmap) {
        // Salvar a imagem no dispositivo
        val uri = salvarImagemNoDispositivo(imagem)

        // Compartilhar a imagem
        if (uri != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, uri)

            val chooser = Intent.createChooser(intent, "Compartilhar imagem via")
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(chooser)
            }
        }
    }
    private fun salvarImagemNoDispositivo(imagem: Bitmap): Uri? {
        // Implementar a lógica para salvar a imagem no dispositivo e obter a URI
        // Este exemplo usa a MediaStore para salvar a imagem no diretório de imagens
        // e obter a URI da imagem recém-salva

        // Salvar a imagem na galeria
        val uri = MediaStore.Images.Media.insertImage(
            contentResolver, imagem, "ImagemComFrase", "Imagem gerada com frase"
        )

        return Uri.parse(uri)
    }
    private fun addTextInImage(imagem:Bitmap, text: String, fatorFloat: Float, fontColor: Int): Bitmap{

        val imagemComFrase = imagem.copy(Bitmap.Config.ARGB_8888, true)

        paint.color = fontColor
        paint.textSize = 50f * fatorFloat


        val canvas = Canvas(imagemComFrase)

        val maxWidth = imagemComFrase.width * 0.8f

        val linhas = quebrarFraseEmLinhas(text, paint, maxWidth)

        val x = (imagemComFrase.width - maxWidth) / 2
        val yInicial = (imagemComFrase.height - linhas.size * (paint.descent() - paint.ascent())) / 2f

        var y = yInicial
        for (linha in linhas) {
            val textWidth = paint.measureText(linha)
            val xOffset = (maxWidth - textWidth) / 2
            canvas.drawText(linha, x + xOffset, y, paint)
            y += paint.descent() - paint.ascent()
        }

        return imagemComFrase
    }

    private fun quebrarFraseEmLinhas(frase: String, paint: Paint, maxWidth: Float): List<String> {
        val linhas = mutableListOf<String>()
        var start = 0
        var end: Int

        while (start < frase.length) {
            end = paint.breakText(frase, start, frase.length, true, maxWidth, null)
            linhas.add(frase.substring(start, start + end))
            start += end
        }

        return linhas
    }

    private fun getFrase(){
        binding.fraseEdit.text = frase + "\n -$autor"
    }

}