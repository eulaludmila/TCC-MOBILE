package tcc.sp.senai.br.showdebolos.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import tcc.sp.senai.br.showdebolos.R
import android.content.SharedPreferences
import android.support.annotation.RequiresApi
import android.util.Log
import android.widget.Button
import tcc.sp.senai.br.showdebolos.CarrinhoFragment
import tcc.sp.senai.br.showdebolos.dao.ProdutoDAO
import tcc.sp.senai.br.showdebolos.model.ItemPedido
import tcc.sp.senai.br.showdebolos.model.Pedido
import tcc.sp.senai.br.showdebolos.model.ProdutoDTO


class AguardandoRespostaAdapter (private val pedidos: List<Pedido>,
                              private val context: Context) : RecyclerView.Adapter<AguardandoRespostaAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_aguardando_resposta, viewGroup, false)

        return ViewHolder(view)

    }

    @RequiresApi(28)
    override fun getItemCount(): Int {

        return if (pedidos != null) pedidos.size else 0
    }

    fun formatarData(dataBanco:String):String{

        val date = dataBanco.split("-", " ")

        val ano = date [0]
        val mes = date [1]
        val dia = date [2]

        val dataBR = "${dia}/${mes}/${ano}"

        return dataBR

    }


    fun formatarHora(dataBanco:String): String {

        val date = dataBanco.split("-",":")

        val hora = date [3]
        val minuto = date [4]

        val horaBR = "${hora}:${minuto}"

        return horaBR

    }

    @RequiresApi(28)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {




        holder.nomeCliente.text = "${pedidos[position].cliente.nome} ${pedidos[position].cliente.sobrenome}"
        holder.codPedido.text = "PEDIDO ${pedidos[position].codPedido}"
        holder.dataEntrega.text = "${formatarData(pedidos[position].dataEntrega)}\n${formatarHora(pedidos[position].dataEntrega)}"

        if(pedidos[position].tipoPagamento.equals("C")){
            holder.tipoPagamento.text = "Cartão de Crédito"
        } else {
            holder.tipoPagamento.text = "Boleto"
        }

        holder.total.text = "${pedidos[position].valorTotal}"


    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var codPedido:TextView = itemView.findViewById(R.id.txt_cod_pedido)
        var nomeCliente:TextView = itemView.findViewById(R.id.txt_pedido_nome_cliente)
        var dataEntrega:TextView = itemView.findViewById(R.id.txt_pedido_data_entrega)
        var tipoPagamento:TextView = itemView.findViewById(R.id.txt_pedido_tipo_pagamento)
        var total:TextView = itemView.findViewById(R.id.txt_pedido_valor_total)
        //var quantidade:TextView = itemView.findViewById(R.id.txt_pedido_quantidade)
//        //        var avaliacao: RatingBar = itemView.findViewById(R.id.rt_avaliacao_produto_home)
//        var fotoProduto: ImageView = itemView.findViewById(R.id.img_produto_carrinho)
//
//        var btnExcluir:ImageView = itemView.findViewById(R.id.img_excluir)



    }
}