package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_visualizar_produto.*
import org.jetbrains.anko.toast
import tcc.sp.senai.br.showdebolos.model.Cidade
import tcc.sp.senai.br.showdebolos.model.Produto

class VisualizarProdutoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_produto)


        val img = findViewById<ImageView>(R.id.expandedImage)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)


        val produto = intent.getSerializableExtra("produto") as Produto

        var min = produto.quantidade.multiplo
        var max = produto.quantidade.maximo
        val list = mutableListOf<String>()
        val listQtde = mutableListOf<Int>()

        val categoria = produto.categoria.categoria

        if(categoria == "Bolo Simples"){
            if(min == 1){
                min++
                list.add("1" + " Unidade")
                listQtde.add(1)
                for( x in min..max){
                    list.add(x.toString() + " Unidades")
                    listQtde.add(x)
                }
            }else{
                for( x in min..max step min){
                    list.add(x.toString() + " Unidades")
                    listQtde.add(x)
                }
            }


        }else if(categoria == "Doce"){
            if(min == 1){
                min++
                list.add("1" + " Unidade")
                listQtde.add(1)
                for( x in min..max){
                    list.add(x.toString() + " Unidades")
                    listQtde.add(x)
                }
            }else{
                for( x in min..max step min){
                    list.add(x.toString() + " Unidades")
                    listQtde.add(x)
                }
            }

        }else if(categoria == "Bolo recheado"){
            for( x in min..max){
                list.add(x.toString() + " Kg")
                listQtde.add(x)
            }
        }

        var position = spn_quantidade.selectedItemPosition
        if(position >= 0){

            var valor_selecionado = list[position]
            Log.d("valor22222222",valor_selecionado)
        }

        if(spn_quantidade.selectedItemPosition>0){
            var posicao = spn_quantidade.selectedItemPosition

            Log.d("aqki",listQtde[posicao].toString())
        }


//        toast(itemSelecionado)




        val adapter = ArrayAdapter(this, R.layout.spinner_adapter_preto ,list )

        spn_quantidade.adapter = adapter

        txt_nome_produto_visualizar.text = produto.nomeProduto
        txt_descricao_produto.text = produto.descricao
        txt_preco_produto_visualizar.text = "R$: " + produto.preco.toString()
        txt_nome_confeiteiro_visualizar.text = produto.confeiteiro.nome
        txt_nome_categoria.text = produto.categoria.categoria
        rt_visualizar_produto.progress = produto.avaliacao.toInt()
        Picasso.with(expandedImage.context).load("http://3.232.178.219${produto.foto}").into(expandedImage)
        Picasso.with(img_confeiteiro_visualizar.context).load("http://3.232.178.219${produto.confeiteiro.foto}").into(img_confeiteiro_visualizar)
        Picasso.with(img_categoria_visualizar.context).load("http://3.232.178.219${produto.categoria}").into(img_categoria_visualizar)

        spn_quantidade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            var total = 0.0
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                var minimo = produto.quantidade.multiplo
                var maximo = produto.quantidade.maximo
                val preco = produto.preco

                if(categoria == "Bolo Simples"){

                    total = listQtde[position] * preco

                }else if(categoria == "Doce"){

                    total = (listQtde[position] / minimo) * preco

                }else if(categoria == "Bolo recheado"){

                    total = (preco / minimo) * listQtde[position]
                }

                Log.d("valor", total.toString())
            }

        }

        setSupportActionBar(toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)



        toolbar.setNavigationOnClickListener {
            view -> onBackPressed()
            finish()
        }
    }



}


