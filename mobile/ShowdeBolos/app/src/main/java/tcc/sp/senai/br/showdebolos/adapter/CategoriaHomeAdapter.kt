package tcc.sp.senai.br.showdebolos.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import tcc.sp.senai.br.showdebolos.ProdutosCategoriaActivity
import tcc.sp.senai.br.showdebolos.R
import tcc.sp.senai.br.showdebolos.model.Categoria


class CategoriaHomeAdapter (private val categorias:List<Categoria>,
                            private val context: Context, val categoriaOnlickListener: CategoriaOnlickListener) : RecyclerView.Adapter<CategoriaHomeAdapter.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_home_categorias, viewGroup, false)

        return ViewHolder(view)

    }

    override fun getItemCount() = if (categorias != null) categorias.size else 0

    //Interface para expor os eventos do adapter
    interface CategoriaOnlickListener {

        fun onClickCategoria(view: View, index: Int)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.nome.text = categorias[position].categoria

        holder.itemView.setOnClickListener {

            val intent = Intent(context!!, ProdutosCategoriaActivity::class.java)
            intent.putExtra("categoria",Categoria(categorias[position].codCategoria,categorias[position].categoria,categorias[position].tipoUnidade))
            context!!.startActivity(intent)

        }



    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val foto: ImageView = itemView.findViewById(R.id.image_categoria_home)
        val nome: TextView = itemView.findViewById(R.id.txt_categoria_home)

    }
}