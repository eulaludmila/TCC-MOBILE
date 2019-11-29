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


    @RequiresApi(28)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        holder.nomeCliente.text = "${pedidos[position].cliente.nome} ${pedidos[position].cliente.sobrenome}"
        holder.codPedido.text = pedidos[position].codPedido.toString()
        holder.dataEntrega.text = pedidos[position].dataEntrega
        holder.tipoPagamento.text = pedidos[position].tipoPagamento.toString()
        holder.total.text = pedidos[position].valorTotal.toString()

//        holder.quantidade.text = produtos[position].quantidade
//
//        var url = produtos[position].foto
//        Picasso.with(holder!!.fotoProduto.context).cancelRequest(holder!!.fotoProduto)
//        Picasso.with(holder!!.fotoProduto.context).load("http://3.232.178.219$url").into(holder!!.fotoProduto)
//
//        holder.btnExcluir.setOnClickListener {
//
//            val dao = ProdutoDAO(context)
//
//            dao.excluir(produtos[position])
//
//            val listaProdutos = dao.getProdutos()
//
//
//
//        }




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