package tcc.sp.senai.br.showdebolos

import android.app.Application

class MyApp: Application() {

    public var isOk = false

    override fun onCreate() {
        super.onCreate()
        isOk = false
    }

}