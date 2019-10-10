package tcc.sp.senai.br.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import tcc.sp.senai.br.showdebolos.R
import tcc.sp.senai.br.showdebolos.model.ConfeiteiroDTO

class ProdutoHomeAdapter (private val confeiteiros:List<String>,
                          private val context: Context) : RecyclerView.Adapter<ProdutoHomeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_home_produtos, viewGroup, false)

        return ViewHolder(view)

    }

    override fun getItemCount(): Int {

        return if (confeiteiros != null) confeiteiros.size else 0
    }

    //Interface para expor os eventos do adapter
    interface CarroOnlickListener {

        fun onClickCarro(view: View, index: Int)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {




        //holder.avaliacao.setProgress(confeiteiros.get(position).avaliacao)

    }




    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    }
}