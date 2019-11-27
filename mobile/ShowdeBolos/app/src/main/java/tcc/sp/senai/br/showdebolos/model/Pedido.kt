package tcc.sp.senai.br.showdebolos.model

import java.util.*

data class Pedido (val codPedido:Int,
                   val valorTotal:Float,
                   val dataSolicitacao: String,
                   val dataEntrega: String,
                   val tipoPagamento: Char,
                   val status:Char,
                   val aprovacao:Char,
                   val observacao:String,
                   val cliente:Cliente){
}
