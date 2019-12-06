package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable

data class Cliente (val codCliente: Int,
                    val nome: String,
                    val sobrenome: String,
                    val cpf: String,
                    val dtNasc: String,
                    val email: String,
                    val senha: String,
                    val celular: Celular,
                    val sexo: String,
                    val foto: String): Serializable{



}