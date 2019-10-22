package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable

data class Endereco ( val codEndereco:Int,
                      var endereco: String,
                      var numero: String,
                      var complemento:String,
                      var cep: String,
                    var bairro: String,
                    val cidade: Cidade):Serializable{



}