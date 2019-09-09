package tcc.sp.senai.br.showdebolos.model

data class Confeiteiro (val codConfeiteiro: Int,
                    val nome: String,
                    val sobrenome: String,
                    val cpf: String,
                    val dtNasc: String,
                    val email: String,
                    val senha: String,
                    val codCelular: Celular,
                    val sexo: Char,
                    val foto: String){



}