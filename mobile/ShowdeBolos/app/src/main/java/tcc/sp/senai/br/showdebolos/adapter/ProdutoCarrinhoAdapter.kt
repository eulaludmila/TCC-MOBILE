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
import tcc.sp.senai.br.showdebolos.model.ProdutoDTO


class ProdutoCarrinhoAdapter (private val produtos: List<ProdutoDTO>,
                              private val context: Context) : RecyclerView.Adapter<ProdutoCarrinhoAdapter.ViewHolder>() {




    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_produtos_carrinho, viewGroup, false)

        return ViewHolder(view)

    }

    @RequiresApi(28)
    override fun getItemCount(): Int {

        return if (produtos != null) produtos.size else 0
    }


    @RequiresApi(28)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        Log.d("PRODUTO_ADICIONADO", produtos.toString())

        holder.nomeProduto.text = produtos[position].nomeProduto
        holder.precoProduto.text = "R$: " + produtos[position].preco.toString()
        holder.quantidade.text = produtos[position].quantidade

        var url = produtos[position].foto
        Picasso.with(holder!!.fotoProduto.context).cancelRequest(holder!!.fotoProduto)
        Picasso.with(holder!!.fotoProduto.context).load("http://3.232.178.219$url").into(holder!!.fotoProduto)

        holder.btnExcluir.setOnClickListener {

            val dao = ProdutoDAO(context)

            dao.excluir(produtos[position])

            val listaProdutos = dao.getProdutos()



        }




    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var nomeProduto:TextView = itemView.findViewById(R.id.txt_nome_produto_carrinho)
        var precoProduto:TextView = itemView.findViewById(R.id.txt_preco_carrinho)
        var quantidade:TextView = itemView.findViewById(R.id.txt_quantidade_carrinho)
        //        var avaliacao: RatingBar = itemView.findViewById(R.id.rt_avaliacao_produto_home)
        var fotoProduto: ImageView = itemView.findViewById(R.id.img_produto_carrinho)

        var btnExcluir:ImageView = itemView.findViewById(R.id.img_excluir)



    }
}