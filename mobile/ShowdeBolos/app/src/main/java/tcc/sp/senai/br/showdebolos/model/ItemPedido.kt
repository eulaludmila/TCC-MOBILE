package tcc.sp.senai.br.showdebolos.model

import java.util.*

data class ItemPedido (val codItemPedido:Int,
                   val produto:ProdutoDTO,
                   val quantidade: String,
                   val valor:Double,
                   val pedido: Pedido){
}