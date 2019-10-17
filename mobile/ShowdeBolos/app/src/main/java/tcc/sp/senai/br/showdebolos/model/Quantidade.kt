package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable

data class Quantidade(val codQuantidade: Int,
                      val multiplo: Int,
                      val maximo: Int): Serializable {
}