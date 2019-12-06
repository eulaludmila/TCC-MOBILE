package tcc.sp.senai.br.showdebolos.adapter

import android.content.Context
import android.content.Intent
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
import tcc.sp.senai.br.showdebolos.DetalhePedidoActivity
import tcc.sp.senai.br.showdebolos.dao.ProdutoDAO
import tcc.sp.senai.br.showdebolos.model.ItemPedido
import tcc.sp.senai.br.showdebolos.model.Pedido
import tcc.sp.senai.br.showdebolos.model.ProdutoDTO


class StatusPedidoClienteAdapter (private val pedidos: List<Pedido>,
                                 private val context: Context) : RecyclerView.Adapter<StatusPedidoClienteAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.adapter_status_pedido_cliente, viewGroup, false)

        return ViewHolder(view)

    }

    @RequiresApi(28)
    override fun getItemCount(): Int {

        return if (pedidos != null) pedidos.size else 0
    }


    fun formatarData(dataBanco: String): String {

        val date = dataBanco.split("-", " ")

        val ano = date[0]
        val mes = date[1]
        val dia = date[2]

        val dataBR = "${dia}/${mes}/${ano}"

        return dataBR

    }


    fun formatarHora(dataBanco: String): String {

        val date = dataBanco.split("-", ":", " ")

        val hora = date[3]
        val minuto = date[4]

        val horaBR = "${hora}:${minuto}"

        return horaBR

    }

    @RequiresApi(28)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        //holder.nomeCliente.text = "${pedidos[position].cliente.nome} ${pedidos[position].cliente.sobrenome}"
        holder.codPedido.text = "Nº ${pedidos[position].codPedido}"
        holder.dataPedido.text = "Pedido efetuado em ${formatarData(pedidos[position].dataSolicitacao)}"

        when {
            pedidos[position].status == 'E' -> holder.status.text = "Esperando resposta do confeiteiro"
            pedidos[position].status == 'A' -> holder.status.text = "Pedido aprovado"
            pedidos[position].status == 'R' -> holder.status.text = "Pedido recusado"
        }

        holder.itemView.setOnClickListener {

            val intent = Intent(context, DetalhePedidoActivity::class.java)
            intent.putExtra("pedido", Pedido(pedidos[position].codPedido,pedidos[position].valorTotal,pedidos[position].dataSolicitacao,
                                            pedidos[position].dataEntrega,pedidos[position].tipoPagamento,pedidos[position].status,pedidos[position].aprovacao,
                                            pedidos[position].observacao,pedidos[position].producao,pedidos[position].cliente))
            context.startActivity(intent)

        }

        holder.total.text = "R$ ${pedidos[position].valorTotal}0".replace(".",",")


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var codPedido: TextView = itemView.findViewById(R.id.txt_numero_pedido)
        var total: TextView = itemView.findViewById(R.id.txt_preco_pedido)
        var status:TextView = itemView.findViewById(R.id.txt_pedido_status_cliente)
        var dataPedido:TextView = itemView.findViewById(R.id.txt_data_pedido)



    }

}