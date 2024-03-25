package dev.crsi.manders.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.R
import dev.crsi.manders.databinding.ActivityRequestBinding
import dev.crsi.manders.providers.SharedPreferenceManager

class RequestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRequestBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private var id_service = 0
    private var id_user = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestBinding.inflate(LayoutInflater.from(this))
        sharedPref = SharedPreferenceManager(this)

        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        getIntents()
        getPrefs()
        setListeners()
    }

    private fun setListeners() {
        binding.buttonEnviar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmación de envío")
                .setMessage(getString(R.string.msj_confirmacion_envio))
                .setPositiveButton("Sí") { dialogInterface, i ->
                    // Enviar el mandado
                    // ...

                    // Mostrar Toast de éxito
                    Toast.makeText(this, R.string.msj_envio_exitoso, Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("No", null)
                .show()
        }


    }

    private fun getIntents() {
        if (intent != null && intent.hasExtra("id_service")) {
            id_service = intent.getIntExtra("id_service", 0)
        } else {
            id_service = 0
            throw IllegalArgumentException("Intent no contiene el extra 'id_service'")
        }
    }

    private fun getPrefs() {
        id_user = sharedPref.getPref("id_user", 0) as Int
        Log.d("getPrefs", "id_user:$id_user")
    }
}