package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable
import java.util.*

data class Pedido (val codPedido:Int,
                   val valorTotal:Double,
                   val dataSolicitacao: String,
                   val dataEntrega: String,
                   val tipoPagamento: Char,
                   val status:Char,
                   val aprovacao:Char,
                   val observacao:String,
                   val producao:String?,
                   val cliente:Cliente):Serializable{
}
