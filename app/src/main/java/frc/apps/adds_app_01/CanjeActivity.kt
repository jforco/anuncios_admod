package frc.apps.adds_app_01

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.json.JSONObject

class CanjeActivity : AppCompatActivity(), View.OnClickListener {
    val url = "https://4dxycvuptj.execute-api.us-east-1.amazonaws.com/deploy/"
    private var cantMonedas: TextView? = null
    private var mensajeFinal: TextView? = null
    private var button_canjear: Button? = null
    private var telefono : EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canje_activity)

        cantMonedas = findViewById(R.id.cant_monedas)
        mensajeFinal = findViewById(R.id.mensaje_final)
        button_canjear = findViewById(R.id.button_canjear)
        button_canjear!!.setOnClickListener(this)
        telefono = findViewById(R.id.telefono_canje)
        val cache: SharedPreferences = getSharedPreferences("cache", MODE_PRIVATE)
        var monedas = cache.getInt("monedas", 0)
        cantMonedas!!.text = monedas.toString()

    }

    override fun onClick(v: View?) {
        val recarga = 5
        val telefono =  telefono!!.text
        val queue = Volley.newRequestQueue(this)

        var json = JSONObject()
        json.put("telefono", telefono)
        json.put("recarga", 5)

        val jsonObjectReq = object : JsonObjectRequest(Method.POST, url, json,
            Response.Listener<JSONObject> { response ->
                Log.d("RESPONSE", response.toString())
                if(response.getString("mensaje") == "exito"){
                    mensajeFinal!!.text = "Tu solicitud se guardado.  En unas horas recibiras tu recarga!"
                    val cache: SharedPreferences = getSharedPreferences("cache", MODE_PRIVATE)
                    cache.edit().clear().apply()
                    val handler = Handler()

                    handler.postDelayed(Runnable() {
                        run() {
                            startActivity<MainActivity>()
                            this.finish()
                        }
                    }, 2000)
                } else {
                    mensajeFinal!!.text = "Algo ha salido mal"
                }
            },
            Response.ErrorListener { error ->
                toast("no se pudo completar la solicitud, intente nuevamente")
                error.printStackTrace()
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        queue.add(jsonObjectReq)
    }
}
