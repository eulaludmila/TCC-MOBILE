package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable

data class Categoria(val codCategoria: Int,
                     val categoria: String,
                     val tipoUnidade: String): Serializable {
}