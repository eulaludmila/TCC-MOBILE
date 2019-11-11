package tcc.sp.senai.br.showdebolos.model

data class ProdutoDTO (val codProduto:Int,
                       val nomeProduto:String,
                       val descricao:String,
                       val codConfeiteiro:Int,
                       val foto:String,
                       val precoTotal:Double,
                       val avaliacao:Double,
                       val quantidade:String){
}

