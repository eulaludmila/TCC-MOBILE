package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable

data class Confeiteiro (val codConfeiteiro: Int,
                    val nome: String,
                    val sobrenome: String,
                    val cpf: String,
                    val dtNasc: String,
                    val email: String,
                    val senha: String,
                    val celular: Celular,
                    var foto: String,
                    val sexo: String,
                    val avaliacao: Double) : Serializable{



}