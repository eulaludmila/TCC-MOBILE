package tcc.sp.senai.br.showdebolos.model

data class Produto(val codProduto: Int,
                   var nomeProduto: String,
//                   var descricao: String,
                   var foto: String,
                   var preco: Double,
//                   var codConfeiteiro: Confeiteiro,
//                   var codCategoria: Categoria,
//                   var codQuantidade: Quantidade,
                   var status: Boolean){
}