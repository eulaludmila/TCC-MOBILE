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
import tcc.sp.senai.br.showdebolos.model.ProdutoDTO


class ProdutoDetalheAdapter (private val produtos: List<ItemPedido>,
                              private val context: Context) : RecyclerView.Adapter<ProdutoDetalheAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_produtos_detalhe, viewGroup, false)

        return ViewHolder(view)

    }

    @RequiresApi(28)
    override fun getItemCount(): Int {

        return if (produtos != null) produtos.size else 0
    }


    @RequiresApi(28)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.nomeProduto.text = produtos[position].produto.nomeProduto
        holder.quantidade.text = "Quantidade: ${produtos[position].quantidade}"
        holder.preco.text = "R$ ${produtos[position].valor}"


        var url = produtos[position].produto.foto
        Picasso.with(holder!!.fotoProduto.context).cancelRequest(holder!!.fotoProduto)
        Picasso.with(holder!!.fotoProduto.context).load("http://3.232.178.219$url").into(holder!!.fotoProduto)

    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var nomeProduto:TextView = itemView.findViewById(R.id.txt_nome_produto_detalhe)
        var quantidade:TextView = itemView.findViewById(R.id.txt_quantidade_detalhe)
        var preco:TextView = itemView.findViewById(R.id.txt_preco_detalhe)
        var fotoProduto:ImageView = itemView.findViewById(R.id.img_produto_detalhe)


    }
}