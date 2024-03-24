package dev.crsi.manders.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.crsi.manders.R
import dev.crsi.manders.adapters.RequestManderAdapter
import dev.crsi.manders.databinding.ActivityMainManderBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.DetailRequestResponse
import dev.crsi.manders.models.ManderActiveRequest
import dev.crsi.manders.models.ManderResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainManderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainManderBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private var id_user = 0
    private var id_mander = 0
    private var is_active_mander = false
    private var filteredRequest = emptyList<DetailRequestResponse>()
    private lateinit var rvRequest: RecyclerView
    private lateinit var requestManderAdapter: RequestManderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainManderBinding.inflate(LayoutInflater.from(this))
        sharedPref = SharedPreferenceManager(this)

        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        getPrefs()
        getInfoMander()

        setListener()
    }

    private fun setListener() {
        binding.switchUpdateStatus.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked != is_active_mander) {
                val manderActiveRequest = ManderActiveRequest(isChecked)
                updateStatus(manderActiveRequest)
            }
        }
    }

    private fun updateStatus(checked: ManderActiveRequest) {

        apiService.updateActiveMander(id_mander, checked)
            .enqueue(object : Callback<ManderResponse> {
                override fun onResponse(
                    call: Call<ManderResponse>,
                    response: Response<ManderResponse>
                ) {
                    if (response.isSuccessful) {

                        val manderList = response.body()!!
                        sharedPref.savePref("id_mander", manderList.id_mander)
                        is_active_mander = manderList.isactive_mander

                    } else {
                        Toast.makeText(
                            this@MainManderActivity,
                            getString(R.string.msj_error_update_status),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    Log.d("registerUserResponse", "onResponse: ${response.body()?.id_mander}")
                    Log.d("registerUserResponse", "onResponse: ${response.body()}")
                    Log.d("registerUserResponse", "onResponse: $response")
                }

                override fun onFailure(call: Call<ManderResponse>, t: Throwable) {
                    Log.e("registerUserFailure", "onFailure: $t")
                }
            })

    }

    private fun getInfoMander() {
        apiService.getManders().enqueue(object : Callback<List<ManderResponse>> {
            override fun onResponse(
                call: Call<List<ManderResponse>>,
                response: Response<List<ManderResponse>>
            ) {
                if (response.isSuccessful) {
                    val manderList = response.body()!!
                    val filteredMander = manderList.filter { it.user_id_user == id_user }

                    filteredMander.forEach { mander ->
                        id_mander = mander.id_mander
                        sharedPref.savePref("id_mander", id_mander)
                        is_active_mander = mander.isactive_mander
                        //savePrefers(mander.id_user)
                        Log.d("data", "isActive:$mander")
                    }
                    getPrefs()
                    Log.d("data", "isActive:$is_active_mander")
                    binding.switchUpdateStatus.isChecked = is_active_mander
                }
            }

            override fun onFailure(call: Call<List<ManderResponse>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getPrefs() {
        id_user = sharedPref.getPref("id_user", 0) as Int
        Log.d("getPrefs", "id_user:$id_user")
        id_mander = sharedPref.getPref("id_mander", 0) as Int
        Log.d("getPrefs", "id_mander:$id_mander")

        getAllRequest()
    }

    private fun getAllRequest() {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        apiService.getRequest().enqueue(object : Callback<List<DetailRequestResponse>> {
            override fun onResponse(
                call: Call<List<DetailRequestResponse>>,
                response: Response<List<DetailRequestResponse>>,
            ) {
                if (response.isSuccessful) {
                    val requestList = response.body()!!
                    filteredRequest = requestList.filter { it.status_request == "Pendiente" }
                    setDataRequest()
                }
            }

            override fun onFailure(call: Call<List<DetailRequestResponse>>, error: Throwable) {
                Toast.makeText(
                    this@MainManderActivity, "Error: ${error.message}", Toast.LENGTH_SHORT
                ).show()
            }


        })
    }

    private fun setDataRequest() {
        rvRequest = binding.recyclerViewRequests
        rvRequest.layoutManager = LinearLayoutManager(this)
        rvRequest.setHasFixedSize(true)
        requestManderAdapter = RequestManderAdapter(filteredRequest)
        rvRequest.adapter = requestManderAdapter
    }
}