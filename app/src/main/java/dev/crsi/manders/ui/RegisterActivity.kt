package dev.crsi.manders.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.databinding.ActivityRegisterBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        sharedPref = SharedPreferenceManager(this)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
    }

}