package com.app.frasestheimpact.utlis

data class DadosApi(
    var status: String? = null,
    var statusMensagem: String? = null,
    var resposta: ArrayList<DadosDaFrase>? = null
)
