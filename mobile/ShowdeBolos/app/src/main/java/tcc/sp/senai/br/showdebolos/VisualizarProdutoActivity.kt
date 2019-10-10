package tcc.sp.senai.br.showdebolos

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.ImageView
import org.jetbrains.anko.toast

class VisualizarProdutoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visualizar_produto)


        val img = findViewById<ImageView>(R.id.expandedImage)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)



        setSupportActionBar(toolbar)

        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar()?.setDisplayShowTitleEnabled(false);


        img.setImageResource(R.drawable.bolo)


        toolbar.setNavigationOnClickListener {
            view -> onBackPressed()
            toast("sfdfdf")
        }


        img.setImageResource(R.drawable.bolo)


    }
}
