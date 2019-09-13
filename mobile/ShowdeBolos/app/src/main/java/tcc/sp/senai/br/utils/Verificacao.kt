package tcc.sp.senai.br.utils

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
        fun verificarSexo(sexo:String):Char{

            if(sexo == "Feminino"){
                return 'F'
            } else {
                return 'M'
            }

        }

        /**
         *  método que verifica se os dados digitados nos campos "senha" e "confirmar senha"
         *  são iguais, para que o cadastro possa ser efetivado
         *
         *  recebe o que foi digitado nos campos, se foram iguais retornará um Boolean
         *  dizendo se são (true) ou não (false)  iguais
         *
         * */
        fun verificarSenha(senha:String, confirmarSenha:String):Boolean{

            val senhaVerificada = senha == confirmarSenha

            return senhaVerificada
        }

    }


}