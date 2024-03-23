package dev.crsi.manders.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.crsi.manders.adapters.MandersAdapter
import dev.crsi.manders.databinding.ActivityAcceptRequetsBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.DetailRequestResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class AcceptRequetsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAcceptRequetsBinding

    private lateinit var rcvManders: RecyclerView
    private lateinit var mandersAdapter: MandersAdapter
    private var listManders = emptyList<DetailRequestResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAcceptRequetsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    private fun initUI() {
        initGetManders()
    }

    //iniciamos la peticion al servidor para obtener los datos
    private fun initGetManders() {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        apiService.getRequest().enqueue(object : Callback<List<DetailRequestResponse>> {
            override fun onResponse(
                call: Call<List<DetailRequestResponse>>,
                response: Response<List<DetailRequestResponse>>,
            ) {
                if (response.isSuccessful) {
                    listManders = response.body()!!

                    if (listManders.isNotEmpty())
                        initRcvManders()
                }
            }

            override fun onFailure(call: Call<List<DetailRequestResponse>>, error: Throwable) {
                Toast.makeText(
                    this@AcceptRequetsActivity,
                    "Error: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }


        })
    }

    private fun initRcvManders() {
        rcvManders = binding.Mandados
        rcvManders.layoutManager = LinearLayoutManager(this)
        rcvManders.setHasFixedSize(true)
        mandersAdapter = MandersAdapter(listManders)
        rcvManders.adapter=mandersAdapter

    }


}


