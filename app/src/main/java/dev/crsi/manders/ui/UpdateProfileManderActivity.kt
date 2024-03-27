package dev.crsi.manders.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.crsi.manders.adapters.MandersAdapter
import dev.crsi.manders.adapters.MandersListAdapter
import dev.crsi.manders.databinding.ActivityAcceptRequetsBinding
import dev.crsi.manders.databinding.ActivityUpdateProfileManderBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.DetailRequestResponse
import dev.crsi.manders.models.ManderRequest
import dev.crsi.manders.models.ManderResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import javax.security.auth.login.LoginException

class UpdateProfileManderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileManderBinding

    private var listMander = emptyList<ManderResponse>()
    private var id_account = 0
    private var id_mander = 0
    private lateinit var sharedPref: SharedPreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileManderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = SharedPreferenceManager(this)

        initUI()

    }

    private fun initUI() {
        getPrefs()
        initGetListManders()
        setupClickListener()
    }
        //accion de botones
    private fun setupClickListener() {
        binding.idBtnSaveChanges.setOnClickListener { startUpdateMander() }
        binding.idBtnCancelUpdate.setOnClickListener {
            val intent= Intent(this, MainManderActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
            //alerta de confirmacion
    private fun startUpdateMander() {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Confirme accion")
        alert.setMessage("Esta seguro de actualizar los datos?")
        alert.setPositiveButton("Aceptar") { dialog, _ ->

            val apiService = ApiClient.retrofit.create(ApiService::class.java)
            val newAddress = binding.idTxtAddressMander.text.toString()
            apiService.updateMander(ManderRequest(newAddress), id_mander)
                .enqueue(object : Callback<ManderResponse> {
                    override fun onResponse(
                        call: Call<ManderResponse>,
                        response: Response<ManderResponse>,
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(
                                this@UpdateProfileManderActivity,
                                "Edith complete",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(
                                this@UpdateProfileManderActivity,
                                "No success",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ManderResponse>, t: Throwable) {
                        Toast.makeText(
                            this@UpdateProfileManderActivity,
                            "Ocurrio un error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            dialog.dismiss()
        }
        alert.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }
        alert.create().show()
    }

    //iniciamos la peticion al servidor para obtener los datos
    private fun initGetListManders() {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        apiService.getManders().enqueue(object : Callback<List<ManderResponse>> {

           //recuperamos los datos y filtramos
            override fun onResponse(
                call: Call<List<ManderResponse>>,
                response: Response<List<ManderResponse>>,
            ) {
                if (response.isSuccessful) {
                    listMander = response.body()!!
                    val filteredMander = listMander.filter { it.user_id_user == id_account }
                    Log.d("TAG", "onResponse: $filteredMander")
                    if (filteredMander.isNotEmpty())
                        setDataToUI(filteredMander[0])
                }
            }

            override fun onFailure(call: Call<List<ManderResponse>>, error: Throwable) {
                Toast.makeText(
                    this@UpdateProfileManderActivity,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }


        })
    }

    private fun setDataToUI(mander: ManderResponse) {
        id_mander = mander.id_mander
        binding.idTxtAddressMander.setText(mander.address_mander)
    }


    private fun getPrefs() {
        id_account = sharedPref.getPref("id_account", 0) as Int
    }

}
