package tcc.sp.senai.br.showdebolos

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_configuracoes_fragment.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.adapter.ListaConfiguracaoAdapter
import tcc.sp.senai.br.showdebolos.model.Cliente
import tcc.sp.senai.br.showdebolos.model.EnderecoCliente
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro
import tcc.sp.senai.br.showdebolos.model.ListaConfiguracao
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.utils.JWTUtils

class ConfiguracoesFragment : Fragment() {

    var mPreferences: SharedPreferences? = null
    var mEditor:SharedPreferences.Editor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mPreferences = this!!.activity!!.getSharedPreferences("idValue", 0)
        val token = mPreferences!!.getString("token","")
        val tipoPerfil = mPreferences!!.getString("tipoPerfil","")
        val idPerfil = mPreferences!!.getString("codUsuario","")

        val view = inflater.inflate(R.layout.activity_configuracoes_fragment, container, false)

        val recyclerView = view.findViewById(R.id.recycler_view_configuracoes) as RecyclerView

        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        carregarListar(view, tipoPerfil)

        if(tipoPerfil=="cliente"){

            val callPerfil = ApiConfig.getClienteService().buscarCliente(token, idPerfil)

            callPerfil.enqueue(object : retrofit2.Callback<Cliente>{
                override fun onFailure(call: Call<Cliente>?, t: Throwable?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onResponse(call: Call<Cliente>?, response: Response<Cliente>?) {
                    val perfil = response!!.body()

                    txt_nome.text = perfil!!.nome
//                    txt_uf_perfil.text= perfil!!.endereco.cidade.cidade +" - "+ perfil!!.endereco.cidade.estado.uf
                    Picasso.with(img_perfil.context).load("http://3.232.178.219${perfil.foto}").into(img_perfil)
                }

            })
        }else if (tipoPerfil=="confeiteiro"){

            val callPerfil = ApiConfig.getConfeiteiroService().buscarConfeiteiro(token, idPerfil)

            callPerfil.enqueue(object : retrofit2.Callback<EnderecoConfeiteiro>{
                override fun onFailure(call: Call<EnderecoConfeiteiro>?, t: Throwable?) {

                }

                override fun onResponse(call: Call<EnderecoConfeiteiro>?, response: Response<EnderecoConfeiteiro>?) {
                    val perfil = response!!.body()

                    txt_nome.text = perfil!!.confeiteiro.nome
                    txt_uf_perfil.text= perfil!!.endereco.cidade.cidade +" - "+ perfil!!.endereco.cidade.estado.uf
                    Picasso.with(img_perfil.context).load("http://3.232.178.219${perfil.confeiteiro.foto}").into(img_perfil)
                }
            })
        }

        val toolbar = view.findViewById(R.id.toolbar) as android.support.v7.widget.Toolbar


        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
            toolbar.textAlignment = View.TEXT_ALIGNMENT_CENTER
            (activity as AppCompatActivity).title = "Em andamento"
        }

        toolbar.setNavigationOnClickListener {

            val fragmentManager: FragmentManager = activity!!.supportFragmentManager

            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.replace(R.id.layout_fragment, FirstFragment()).commit()
        }
        return view
    }

    fun carregarListar(view:View, tipoPerfil:String){

        val recyclerView = view.findViewById(R.id.recycler_view_configuracoes) as RecyclerView

        val itens = ArrayList<ListaConfiguracao>()

        val item1 = ListaConfiguracao(R.drawable.ic_bolo,"Produtos","Edição, visualização")
        val item2 = ListaConfiguracao(R.drawable.ic_local, "Endereço", "Adicionar local de entregar")
        val item3 = ListaConfiguracao(R.drawable.ic_historico, "Histórico", "Produtos recentemente comprados")
        val item4 = ListaConfiguracao(R.drawable.ic_produtos_confirmados, "Pedidos", "Confirmação, acompanhamento")
        val item5 = ListaConfiguracao(R.drawable.ic_exit, "Sair", "Fazer logoff")

        if(tipoPerfil=="cliente"){
            itens.add(item2)
            itens.add(item3)
            itens.add(item5)
        }else if(tipoPerfil=="confeiteiro"){
            itens.add(item1)
            itens.add(item4)
            itens.add(item5)
        }


        val listaConfiguracaoAdapter = ListaConfiguracaoAdapter(itens,this!!.context!!)
        recyclerView.adapter = listaConfiguracaoAdapter



    }


}
