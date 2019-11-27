package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable

data class EnderecoCliente(val codEnderecoConfeiteiro: Int,
                           val cliente: Cliente?,
                           val endereco: Endereco):Serializable {
}