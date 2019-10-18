package tcc.sp.senai.br.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import tcc.sp.senai.br.showdebolos.PerfilConfeiteiroActivity
import tcc.sp.senai.br.showdebolos.R
import tcc.sp.senai.br.showdebolos.model.ConfeiteiroDTO
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL

class TodosConfeiteirosAdapter (private var confeiteiros:List<ConfeiteiroDTO>,
                                private var context: Context?,
                                var confeiteiroOnClickListener: ConfeiteiroHomeAdapter.ConfeiteiroOnlickListener) : RecyclerView.Adapter<TodosConfeiteirosAdapter.ViewHolder>() {



    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_todos_confeiteiros, viewGroup, false)

        return ViewHolder(view)

    }


    override fun getItemCount() = if (confeiteiros != null) confeiteiros.size else 0


    //Interface para expor os eventos do adapter
    interface ConfeiteiroOnlickListener {

        fun onClickConfeiteiro(view: View, index: Int)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.nomeConfeiteiro.text = confeiteiros.get(position).nome

        holder.avaliacao.progress = confeiteiros.get(position).avaliacao.toInt()

        //colocando o listener da lista na lista(eu criei o próprio listener)
        holder.itemView.setOnClickListener{

            val intent = Intent(context, PerfilConfeiteiroActivity::class.java)
            intent.putExtra("confeiteiro", ConfeiteiroDTO(confeiteiros[position].codConfeiteiro,confeiteiros[position].nome,confeiteiros[position].sobrenome,
                    confeiteiros[position].dtNasc,confeiteiros[position].celular,confeiteiros[position].foto,
                    confeiteiros[position].sexo,confeiteiros[position].avaliacao))
            context!!.startActivity(intent)

        }


        var url = confeiteiros[position].foto
        Picasso.with(holder!!.foto.context).cancelRequest(holder!!.foto)
        Picasso.with(holder!!.foto.context).load("http://54.242.6.253$url").into(holder!!.foto)

        try{
            val carregarImgTasks = CarregarImgTasks(url)

            carregarImgTasks.holder = holder

            carregarImgTasks.execute()
        }catch (e:MalformedURLException){
            e.printStackTrace()
        }



    }

    class CarregarImgTasks(val caminho: String): AsyncTask<URL, Void, Drawable>() {

        var holder: ViewHolder? = null

        override fun doInBackground(vararg p0: URL?): Drawable? {

            var url = URL("http://54.242.6.253$caminho")
            var content: InputStream?

            try{
                content = url.getContent() as InputStream?

                val drw = Drawable.createFromStream(content, "foto")

                return drw
            }catch (e: IOException){
                e.printStackTrace()
            }

            return null

        }

        override fun onPreExecute() {
            super.onPreExecute()
            holder!!.progressBar.setVisibility(View.VISIBLE)

        }

        override fun onPostExecute(result: Drawable?) {
            super.onPostExecute(result)
//            holder!!.foto.setImageDrawable(result)
            holder!!.progressBar.setVisibility(View.INVISIBLE)

        }

    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        var nomeConfeiteiro: TextView = itemView.findViewById(R.id.txt_nome_todos_confeiteiros)
        var avaliacao: RatingBar = itemView.findViewById(R.id.rt_avaliacao_todos_confeiteiros)
        var progressBar: ProgressBar = itemView.findViewById(R.id.progressImgConfeiteiro)
        var foto: ImageView = itemView.findViewById(R.id.img_todos_confeiteiros)
    }
}