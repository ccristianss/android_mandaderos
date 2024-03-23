package dev.crsi.manders.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.crsi.manders.adapters.MandersAdapter
import dev.crsi.manders.adapters.MandersListAdapter
import dev.crsi.manders.databinding.ActivityAcceptRequetsBinding
import dev.crsi.manders.databinding.ActivityUpdateProfileManderBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.DetailRequestResponse
import dev.crsi.manders.models.ManderResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class UpdateProfileManderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileManderBinding

    private lateinit var rcvMander: RecyclerView
    private lateinit var manderListAdapter: MandersListAdapter
    private var listMander = emptyList<ManderResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileManderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    private fun initUI() {
        initGetlistManders()
    }

    //iniciamos la peticion al servidor para obtener los datos
    private fun initGetlistManders() {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        apiService.getmander().enqueue(object : Callback<List<ManderResponse>> {

            override fun onResponse(
                call: Call<List<ManderResponse>>,
                response: Response<List<ManderResponse>>,
            ) {
                if (response.isSuccessful) {
                    listMander = response.body()!!

                    if (listMander.isNotEmpty())
                        initRcvManders()
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

    private fun initRcvManders() {
        rcvMander = binding.ListManders
        rcvMander.layoutManager = LinearLayoutManager(this)
        rcvMander.setHasFixedSize(true)
        manderListAdapter = MandersListAdapter(listMander)
        rcvMander.adapter=manderListAdapter

    }


}
