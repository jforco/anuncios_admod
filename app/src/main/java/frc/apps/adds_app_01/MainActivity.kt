package frc.apps.adds_app_01

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), View.OnClickListener, RewardedVideoAdListener {

    private var mRewardedVideoAd: RewardedVideoAd? = null
    private var cantMonedas: TextView? = null
    private var button_anuncio:Button? = null
    private var button_canjear:Button? = null
    private val codigo_app = "ca-app-pub-3940256099942544/5224354917"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MobileAds.initialize(this, codigo_app)
        cantMonedas = findViewById(R.id.cant_monedas)
        button_anuncio = findViewById(R.id.button_publicidad)
        button_canjear = findViewById(R.id.button_canjear)
        button_anuncio!!.setOnClickListener(this)
        button_canjear!!.setOnClickListener(this)
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd!!.rewardedVideoAdListener = this
        loadRewardedVideoAd()
        actualizarMonedas(0)
    }

    override fun onClick(v: View?) {
        when (v!!.id){
            R.id.button_publicidad -> {
                if (mRewardedVideoAd!!.isLoaded()) {
                    mRewardedVideoAd!!.show()
                } else {
                    toast("Espere, contenido cargando")
                }
            }
            R.id.button_canjear -> {
                if(cant_monedas.text.toString().toInt() > 999){
                    startActivity<CanjeActivity>()
                    this.finish()
                } else {
                    toast("Aun no has completado el mínimo!")
                }
            }
        }
    }

    private fun loadRewardedVideoAd(){
        mRewardedVideoAd!!.loadAd(codigo_app, AdRequest.Builder().build())
    }

    override fun onRewardedVideoAdClosed() {
        loadRewardedVideoAd();
    }

    override fun onRewardedVideoAdLeftApplication() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRewardedVideoAdLoaded() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRewardedVideoAdOpened() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRewardedVideoCompleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRewardedVideoStarted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRewardedVideoAdFailedToLoad(p: Int) {
        loadRewardedVideoAd()
        Log.d("ERROR", "falla en cargar video:" + p)
    }

    override fun onRewarded(p: RewardItem?) {
        longToast("10 monedas ganadas!")
        Log.d("RECOMPENSA : ", p!!.amount.toString())
        actualizarMonedas(10)
    }

    private fun actualizarMonedas(cant: Int) {
        val cache: SharedPreferences = getSharedPreferences("cache", MODE_PRIVATE)
        var monedas = cache.getInt("monedas", 0)
        //actualizar
        if(cant>0){
            monedas = monedas + cant
            cache.edit().putInt("monedas", monedas).apply()
        }
        cantMonedas!!.text = monedas.toString()
    }
}
