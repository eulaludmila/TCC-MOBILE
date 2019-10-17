package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable

data class Produto(val codProduto: Int,
                   var nomeProduto: String,
                   var descricao: String,
                   var foto: String,
                   var preco: Double,
                   var confeiteiro: Confeiteiro,
                   var categoria: Categoria,
                   var quantidade: Quantidade,
                   var status: Boolean,
                   var avaliacao: Double): Serializable{
}