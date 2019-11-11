package tcc.sp.senai.br.showdebolos.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import tcc.sp.senai.br.showdebolos.R
import tcc.sp.senai.br.showdebolos.VisualizarProdutoActivity
import tcc.sp.senai.br.showdebolos.model.Produto

class ProdutoHomeAdapter (private val produtos:List<Produto>,
                          private val context: Context) : RecyclerView.Adapter<ProdutoHomeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_home_produtos, viewGroup, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {

        return if (produtos != null) produtos.size else 0
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        holder.nomeProduto.text = produtos[position].nomeProduto
        holder.precoProduto.text = "R$: " + produtos[position].preco.toString()
        holder.avaliacao.progress = produtos.get(position).avaliacao.toInt()
        var url = produtos[position].foto
        Picasso.with(holder!!.fotoProduto.context).cancelRequest(holder!!.fotoProduto)
        Picasso.with(holder!!.fotoProduto.context).load("http://3.232.178.219$url").into(holder!!.fotoProduto)

        holder.itemView.setOnClickListener {

            val intent = Intent(context, VisualizarProdutoActivity::class.java)
            intent.putExtra("produto",Produto(produtos[position].codProduto,produtos[position].nomeProduto,produtos[position].descricao,produtos[position].foto,
                    produtos[position].preco,produtos[position].confeiteiro,produtos[position].categoria,produtos[position].quantidade,
                    produtos[position].status, produtos[position].avaliacao))
            context.startActivity(intent)

        }


    }




    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var nomeProduto:TextView = itemView.findViewById(R.id.txt_produto_home)
//        var localizacao:TextView = itemView.findViewById(R.id.txt_produto_localizacao)
        var precoProduto:TextView = itemView.findViewById(R.id.txt_produto_preco_home)
        var avaliacao: RatingBar = itemView.findViewById(R.id.rt_avaliacao_produto_home)
        var fotoProduto: ImageView = itemView.findViewById(R.id.image_produto_home)



    }
}