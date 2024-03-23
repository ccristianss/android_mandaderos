package dev.crsi.manders.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.crsi.manders.R
import dev.crsi.manders.adapters.RequestAdapter
import dev.crsi.manders.adapters.ServicesAdapter
import dev.crsi.manders.databinding.ActivityMainBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.DetailRequestResponse
import dev.crsi.manders.models.ServiceResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private var id_user = 0
    private var filteredRequest = emptyList<DetailRequestResponse>()
    private var servicesList = emptyList<ServiceResponse>()
    private lateinit var rvRequest: RecyclerView
    private lateinit var rvServices: RecyclerView
    private lateinit var requestAdapter: RequestAdapter
    private lateinit var servicesAdapter: ServicesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        sharedPref = SharedPreferenceManager(this)

        setContentView(binding.root)

        initUI()


        //val intent = Intent(this, AcceptRequetsActivity::class.java)
        //finish()
        //startActivity(intent)
    }

    private fun initUI() {
        getPrefs()
        binding.btnMenu.setOnClickListener { view ->
            showPopupMenu(view)
        }

    }

    private fun getPrefs() {
        id_user = sharedPref.getPref("id_user", 0) as Int
        Log.d("getPrefs", "id_user:$id_user")

        getRequestByUser()
        getServices()
    }

    private fun getRequestByUser() {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        apiService.getRequest().enqueue(object : Callback<List<DetailRequestResponse>> {
            override fun onResponse(
                call: Call<List<DetailRequestResponse>>,
                response: Response<List<DetailRequestResponse>>,
            ) {
                if (response.isSuccessful) {
                    val requestList = response.body()!!
                    filteredRequest = requestList.filter { it.user_id_user == id_user }
                    setDataRequest()
                }
            }

            override fun onFailure(call: Call<List<DetailRequestResponse>>, error: Throwable) {
                Toast.makeText(
                    this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT
                ).show()
            }


        })
    }

    private fun setDataRequest() {
        rvRequest = binding.request
        rvRequest.layoutManager = LinearLayoutManager(this)
        rvRequest.setHasFixedSize(true)
        requestAdapter = RequestAdapter(filteredRequest)
        rvRequest.adapter = requestAdapter
    }


    private fun getServices() {
        val apiService = ApiClient.retrofit.create(ApiService::class.java)
        apiService.getService().enqueue(object : Callback<List<ServiceResponse>> {
            override fun onResponse(
                call: Call<List<ServiceResponse>>,
                response: Response<List<ServiceResponse>>,
            ) {
                if (response.isSuccessful) {
                    servicesList = response.body()!!
                    setDataServices()
                }
            }

            override fun onFailure(call: Call<List<ServiceResponse>>, error: Throwable) {
                Toast.makeText(
                    this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT
                ).show()
            }


        })
    }

    private fun setDataServices() {
        rvServices = binding.services
        rvServices.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvServices.setHasFixedSize(true)
        servicesAdapter = ServicesAdapter(servicesList)
        rvServices.adapter = servicesAdapter
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_main)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.edit_profile -> {
                    val intent = Intent(this , UpdateUserProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.change_password -> {
                    val intent = Intent(this , UpdatePasswordActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.mander -> {
                    val intent = Intent(this , MainManderActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }




}