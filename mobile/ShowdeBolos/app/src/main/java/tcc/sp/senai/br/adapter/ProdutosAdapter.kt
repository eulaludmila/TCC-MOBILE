package tcc.sp.senai.br.showdebolos.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tcc.sp.senai.br.showdebolos.R
import tcc.sp.senai.br.showdebolos.model.Produto

class ProdutosAdapter(val produto: List<Produto>): RecyclerView.Adapter<ProdutosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.produtos_adapter, parent , false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {

        return produto.size

    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {

        val p = produto.get(p1)
        holder.nomeProduto.text = p.nomeProduto
        holder.preco.text = p.preco.toString()

    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val nomeProduto: TextView = itemView.findViewById(R.id.txt_nome_produto)
        val preco: TextView = itemView.findViewById(R.id.txt_preco_produto)
        val foto: TextView = itemView.findViewById(R.id.img_produto)
    }

}