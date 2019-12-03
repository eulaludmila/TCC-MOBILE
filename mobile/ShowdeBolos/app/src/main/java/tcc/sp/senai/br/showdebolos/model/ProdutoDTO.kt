package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable

data class ProdutoDTO (val codProduto:Int,
                       val nomeProduto:String,
                       val descricao:String,
                       val confeiteiro:Int,
                       val foto:String,
                       val preco:Double,
                       val avaliacao:Double,
                       val quantidade:String):Serializable{
}

