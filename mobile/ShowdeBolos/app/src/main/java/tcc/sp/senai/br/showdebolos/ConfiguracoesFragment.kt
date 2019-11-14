package tcc.sp.senai.br.showdebolos

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.util.Log
import android.view.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_configuracoes_fragment.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import tcc.sp.senai.br.showdebolos.model.EnderecoConfeiteiro
import tcc.sp.senai.br.showdebolos.services.ApiConfig
import tcc.sp.senai.br.showdebolos.utils.JWTUtils

class ConfiguracoesFragment : Fragment() {

    var mPreferences: SharedPreferences? = null
    var mEditor:SharedPreferences.Editor? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mPreferences = this!!.activity!!.getSharedPreferences("idValue", 0)
        val token = mPreferences!!.getString("token","")

        var parts = token.split(".")

        var json = parts[1]

        val tokenDecod = JWTUtils.getJson(json)

        val jsontoken = JSONObject(tokenDecod)

        Log.d("token22222", tokenDecod.toString())

        var idPerfil  = jsontoken.getString("codUsuario")

        Log.d("token22222",idPerfil.toString())

        mEditor = mPreferences!!.edit()
        mEditor!!.putString("codCliente",idPerfil)
        mEditor!!.commit()


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



        val view = inflater.inflate(R.layout.activity_configuracoes_fragment, container, false)
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


}
