package br.jus.tjro.gabinete.adapters

interface ModeloDocumentoAdapter {
    fun adapt(variaveis: List<String>, assinando : Boolean): HashMap<String, Any>
}
