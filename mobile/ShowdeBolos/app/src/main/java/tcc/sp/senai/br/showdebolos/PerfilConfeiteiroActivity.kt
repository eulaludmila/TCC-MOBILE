package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import kotlinx.android.synthetic.main.activity_perfil_confeiteiro.*
import tcc.sp.senai.br.showdebolos.adapter.ProdutosAdapter
import tcc.sp.senai.br.showdebolos.model.Produto
import tcc.sp.senai.br.showdebolos.tasks.CarregaProdutosTasks
import java.util.*
import kotlin.collections.ArrayList

class PerfilConfeiteiroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_confeiteiro)

        val produto = ArrayList<Produto>()

        val carregarProdutos = CarregaProdutosTasks(produto)
        carregarProdutos.execute()

        val retornoProduto = carregarProdutos.get()

//        recycler_view.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
//        recycler_view.adapter = ProdutosAdapter(retornoProduto)

    }
}
