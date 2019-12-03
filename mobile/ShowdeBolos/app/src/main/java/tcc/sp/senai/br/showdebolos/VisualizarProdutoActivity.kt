package tcc.sp.senai.br.showdebolos

import android.app.AlertDialog
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login_cliente.*
import kotlinx.android.synthetic.main.activity_visualizar_produto.*
import org.jetbrains.anko.toast
import tcc.sp.senai.br.showdebolos.dao.ProdutoDAO
import tcc.sp.senai.br.showdebolos.model.Cidade
import tcc.sp.senai.br.showdebolos.model.Produto
import tcc.sp.senai.br.showdebolos.model.ProdutoDTO

class VisualizarProdutoActivity : AppCompatActivity() {

    var mPreferences: SharedPreferences? = null
    var mEditor: SharedPreferences.Editor? = null

    var total = 0.0
    var quantidade = ""

    @RequiresApi(28)
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

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                var minimo = produto.quantidade.multiplo
                val preco = produto.preco

                if(categoria == "Bolo Simples"){

                    total = listQtde[position] * preco
                    quantidade = list[position]


                }else if(categoria == "Doce"){

                    total = (listQtde[position] / minimo) * preco
                    quantidade = list[position]


                }else if(categoria == "Bolo recheado"){

                    total = (preco / minimo) * listQtde[position]
                    quantidade = list[position]

                }

                Log.d("valor", total.toString())

                btn_add_carrinho.text = "ADICIONAR R$ "+total.toString()+"0"
            }

        }
        mPreferences = getSharedPreferences("idValue", 0)
        mEditor = mPreferences!!.edit()
        val listProduto = mutableSetOf<String>()
        btn_add_carrinho.setOnClickListener {

            val dao = ProdutoDAO(this)

            val produtos = dao.getProdutos()

            Log.d("quantidade_total", quantidade.toString())

            val produtoDTO = ProdutoDTO(produto.codProduto,
                                        produto.nomeProduto,
                                        produto.descricao,
                                        produto.confeiteiro.codConfeiteiro,
                                        produto.foto,
                                        total,
                                        produto.avaliacao,
                                        quantidade)


            try {
                    if(produtos[0].confeiteiro == produto.confeiteiro.codConfeiteiro){
                        for (i in 0 until produtos.size){
                            if(produtos[i].codProduto == produto.codProduto){
                                val builder = AlertDialog.Builder(this@VisualizarProdutoActivity)
                                builder.setTitle("ERRO")
                                builder.setIcon(R.drawable.ic_erro)
                                builder.setMessage("Este produto já esta no seu carrinho.")
                                builder.setPositiveButton("OK"){dialog, which ->  }
                                builder.setNegativeButton("CANCELAR"){dialog, which ->
                                    dao.excluir(produtos[i])
                                    dao.salvar(produtoDTO)
                                }
                                builder.show()
                                break
                            }else{
                                dao.salvar(produtoDTO)
                                Toast.makeText(this, "${produto.nomeProduto} adicionado a sacola", Toast.LENGTH_LONG).show()
                                break
                            }

                        }

                    }else{
                        val builder = AlertDialog.Builder(this@VisualizarProdutoActivity)
                        builder.setTitle("ER" +
                                ",0RO")
                        builder.setIcon(R.drawable.ic_erro)
                        builder.setMessage("Você pode escolher produtos somente do mesmo confeiteiro.\n" +
                                "Deseja trocar de confeiteiro?")
                        builder.setPositiveButton("OK"){dialog, which ->
                            dao.excluirTodos()
                            dao.salvar(produtoDTO)
                        }
                        builder.setNegativeButton("CANCELAR"){dialog, which ->
                        }
                        builder.show()
                    }

            }catch (e:IndexOutOfBoundsException){
                e.printStackTrace()
                dao.salvar(produtoDTO)
                Toast.makeText(this, "${produto.nomeProduto} adicionado a sacola", Toast.LENGTH_LONG).show()
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


