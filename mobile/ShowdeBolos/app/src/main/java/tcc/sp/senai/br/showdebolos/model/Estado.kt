package tcc.sp.senai.br.showdebolos.model

import java.io.Serializable

data class Estado(val codEstado:Int,
                  var uf:String,
                  var estado:String):Serializable{
}