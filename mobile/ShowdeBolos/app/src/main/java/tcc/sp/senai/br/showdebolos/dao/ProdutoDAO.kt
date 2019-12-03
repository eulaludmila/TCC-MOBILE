package tcc.sp.senai.br.showdebolos.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.annotation.RequiresApi
import tcc.sp.senai.br.showdebolos.model.Produto
import tcc.sp.senai.br.showdebolos.model.ProdutoDTO

@RequiresApi(28)
class ProdutoDAO : SQLiteOpenHelper {

    var context: Context? = null

    constructor(context: Context) : super(context, "db_pedido", null, 5) {
        this.context = context
    }


    override fun onCreate(db: SQLiteDatabase?) {

        val sql = "CREATE TABLE tbl_produto(" +
                "cod INTEGER PRIMARY KEY," +
                "cod_produto INTEGER NOT NULL," +
                "nome_produto TEXT NOT NULL," +
                "descricao TEXT NOT NULL," +
                "cod_confeiteiro INTEGER NOT NULL," +
                "foto TEXT NOT NULL," +
                "preco_total DOUBLE NOT NULL," +
                "avaliacao DOUBLE NOT NULL," +
                "quantidade TEXT NOT NULL)"

        db!!.execSQL(sql)
    }

    fun salvar(produto: ProdutoDTO){

        val db = writableDatabase

        val dados = getContentValues(produto)

        db.insert("tbl_produto", null, dados)

    }

    fun excluir(produto: ProdutoDTO){

        val db = writableDatabase

        val params = arrayOf<String>(produto.codProduto.toString())

        db.delete("tbl_produto", "cod_produto = ?", params)
    }

    fun excluirTodos(){

        val db = writableDatabase

        db.delete("tbl_produto", null ,null)
    }

    fun getContentValues(produto:ProdutoDTO):ContentValues{

        val dados = ContentValues()

        dados.put("cod_produto", produto.codProduto)
        dados.put("nome_produto", produto.nomeProduto)
        dados.put("descricao", produto.descricao)
        dados.put("cod_confeiteiro", produto.confeiteiro)
        dados.put("foto", produto.foto)
        dados.put("preco_total", produto.preco)
        dados.put("avaliacao", produto.avaliacao)
        dados.put("quantidade", produto.quantidade)

        return dados
    }

    fun getProdutos(): List<ProdutoDTO> {

        val db = readableDatabase

        val sql = "SELECT * FROM tbl_produto"

        val c = db.rawQuery(sql, null)

        val produtos = ArrayList<ProdutoDTO>()


        while (c.moveToNext()) {

            val produto = ProdutoDTO(
                    c.getInt(c.getColumnIndex("cod_produto")),
                    c.getString(c.getColumnIndex("nome_produto")),
                    c.getString(c.getColumnIndex("descricao")),
                    c.getInt(c.getColumnIndex("cod_confeiteiro")),
                    c.getString(c.getColumnIndex("foto")),
                    c.getDouble(c.getColumnIndex("preco_total")),
                    c.getDouble(c.getColumnIndex("avaliacao")),
                    c.getString(c.getColumnIndex("quantidade")))

            produtos.add(produto);

        }

        return produtos
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        onCreate(db)
    }

}