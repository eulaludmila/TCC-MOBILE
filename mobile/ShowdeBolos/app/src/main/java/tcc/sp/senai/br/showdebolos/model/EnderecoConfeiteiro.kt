package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable

data class EnderecoConfeiteiro(val codEnderecoConfeiteiro: Int,
                                val confeiteiro: Confeiteiro,
                                val endereco: Endereco):Serializable {
}