package dev.crsi.manders.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.R
import dev.crsi.manders.databinding.ActivityLoginBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.AccountRequest
import dev.crsi.manders.models.AccountResponse
import dev.crsi.manders.models.LoginRequest
import dev.crsi.manders.models.LoginResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    private var email = ""
    private var pass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        sharedPref = SharedPreferenceManager(this)
        setContentView(binding.root)

        initUI()

    }

    private fun initUI() {
        setListener()
    }

    private fun setListener() {
        binding.buttonLogin.setOnClickListener {
            if (validateForm()) {
                email = binding.editTextEmail.text.toString()
                pass = binding.editTextPassword.text.toString()
                val loginRequest = LoginRequest(email, pass)

                getIdAccount(loginRequest)
            }
        }
        binding.buttonRegister.setOnClickListener {
            if (validateForm()) {
                email = binding.editTextEmail.text.toString()
                pass = binding.editTextPassword.text.toString()
                val accountRequest = AccountRequest(email, pass)

                registerAccount(accountRequest)
            }
        }
    }

    private fun validateForm(): Boolean {
        return if (binding.editTextEmail.text.toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.msj_empty_email), Toast.LENGTH_SHORT).show()
            binding.editTextEmail.requestFocus()
            false
        } else if (binding.editTextPassword.text.toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.msj_empty_password), Toast.LENGTH_SHORT).show()
            binding.editTextPassword.requestFocus()
            false
        } else {
            true
        }
    }

    private fun getIdAccount(loginRequest: LoginRequest) {
        apiService.login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                // Manejar respuesta
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    if (response.code() == 200) {
                        savePrefers(response.body()?.id_account)
                        GotoRegister()

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error de Autenticacion",
                            Toast.LENGTH_SHORT
                        ).show()
                        activateButtonRegister()
                    }

                    Log.d("getIdAccountonResponse", "onResponse: ${response.body()?.id_account}")
                    Log.d("getIdAccountonResponse", "onResponse: ${response.body()}")
                    Log.d("getIdAccountonResponse", "onResponse: $response")
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                    Log.d("getIdAccountonFailure", "onResponse: $t")
                }
            })
    }

    fun savePrefers(idAccount: Int?) {
        if (idAccount != null) {
            sharedPref.savePref("id_account", idAccount)
        }
        sharedPref.savePref("email_account", email)
        sharedPref.savePref("password_account", pass)
    }

    private fun GotoRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    private fun activateButtonRegister() {
        binding.buttonRegister.visibility = View.VISIBLE
    }


    private fun registerAccount(accountRequest: AccountRequest) {
        apiService.createAccount(accountRequest)
            .enqueue(object : Callback<AccountResponse> {
                // Manejar respuesta
                override fun onResponse(
                    call: Call<AccountResponse>,
                    response: Response<AccountResponse>
                ) {

                    if (response.code() == 201) {
                        savePrefers(response.body()?.id_account)
                        GotoRegister()

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Error de Autenticacion",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    Log.d("registerAccountonRes", "onResponse: ${response.body()?.id_account}")
                    Log.d("registerAccountonRes", "onResponse: ${response.body()}")
                    Log.d("registerAccountonRes", "onResponse: $response")
                }

                override fun onFailure(call: Call<AccountResponse>, t: Throwable) {

                    Log.d("registerAccountonFail", "onResponse: $t")
                }
            })


    }
}
