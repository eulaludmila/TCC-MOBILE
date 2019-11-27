package tcc.sp.senai.br.showdebolos.model

data class Cartao(val numeroCartao:String,
                  val cvv:String,
                  val validade:String,
                  val nomeTitular:String,
                  val cpfTitular:String){
}