package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable

data class ConfeiteiroDTO (val codConfeiteiro: Int,
                        val nome: String,
                        val sobrenome: String,
                        val dtNasc: String,
                        val celular: Celular,
                        val foto: String,
                        val sexo: String,
                           val avaliacao:Double) : Serializable {



}