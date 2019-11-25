package tcc.sp.senai.br.showdebolos.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.adapter_pedidos_pagamento.view.*
import tcc.sp.senai.br.showdebolos.R
import tcc.sp.senai.br.showdebolos.model.Categoria
import tcc.sp.senai.br.showdebolos.model.Pedido

class PagamentoPedidosAdapter (private val pedidos:List<Pedido>,
                               private val context: Context) : RecyclerView.Adapter<PagamentoPedidosAdapter.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int):ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_pedidos_pagamento, viewGroup, false)

        return ViewHolder(view)

    }

    override fun getItemCount()= if(pedidos != null) pedidos.size else 0


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.valorTotal.text = pedidos.get(position).valorTotal.toString()
        holder.codPedido.text = pedidos.get(position).codPedido.toString()


    }



    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var valorTotal: TextView = itemView.findViewById(R.id.txt_valor_total_pedido_pagamento)
        var codPedido:TextView = itemView.findViewById(R.id.txt_cod_pedido)
    }

}