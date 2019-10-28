package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
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

        txt_nome_produto_visualizar.text = produto.nomeProduto
        txt_descricao_produto.text = produto.descricao
        txt_preco_produto_visualizar.text = "R$: " + produto.preco.toString()
        txt_nome_confeiteiro_visualizar.text = produto.confeiteiro.nome
        txt_nome_categoria.text = produto.categoria.categoria
        rt_visualizar_produto.progress = produto.avaliacao.toInt()
        Picasso.with(expandedImage.context).load("http://3.232.178.219${produto.foto}").into(expandedImage)
        Picasso.with(img_confeiteiro_visualizar.context).load("http://3.232.178.219${produto.confeiteiro.foto}").into(img_confeiteiro_visualizar)







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
