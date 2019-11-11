package tcc.sp.senai.br.showdebolos.utils

import android.content.Context

/***
 *
 *  CLASSE QUE CONTEM OS MÉTODOS DE VERIFICAÇAO, COMO SEXO E SENHA CADASTRADA
 *
 * */
class Verificacao {

    /**
     *  companion object{} é utilizado para declarar os métodos estáticos
     *  só é permitido ter um por classe
     *  caso tenha mais de um método que será estático devem ser colocados no mesmo escopo
     *
     * */
    companion object {


        /**
         *  método que verifica qual sexo foi escolhido no spinner de cadastro
         *  recebe o sexo selecionado e retornando um CHAR correspondente a ele
         *  para cadastrar no banco
         *
         * */
        fun verificarSexo(sexo:String):String{

            var retornoSexo: String? = null

            when(sexo){
                "Feminino" -> {
                    retornoSexo = "F"
                }

                "Masculino" -> {
                    retornoSexo = "M"
                }

                "Outros" -> {
                    retornoSexo = "O"
                }

                "Não Informar" -> {
                    retornoSexo = "N"
                }

                "Selecione o Sexo" -> {
                    retornoSexo = "SS"
                }

                else ->{

                }

            }

            return retornoSexo!!

        }

        /**
         *  método que verifica se os dados digitados nos campos "senha" e "confirmar senha"
         *  são iguais, para que o cadastro possa ser efetivado
         *
         *  recebe o que foi digitado nos campos, se foram iguais retornará um Boolean
         *  dizendo se são (true) ou não (false)  iguais
         *
         * */
        fun verificarSenha(senha:String, confirmarSenha:String, context: Context):String{

            var senhaCorreta: String? = null

            if(senha == confirmarSenha){
                senhaCorreta =  senha
            }else{
            }

            return senhaCorreta!!

        }



    }


}