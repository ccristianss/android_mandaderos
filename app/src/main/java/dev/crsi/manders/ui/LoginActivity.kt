package dev.crsi.manders.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.R
import dev.crsi.manders.databinding.ActivityLoginBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.LoginRequest
import dev.crsi.manders.models.LoginResponse
import dev.crsi.manders.models.UserResponse
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
                activateButtonLogin(false)
                email = binding.editTextEmail.text.toString()
                pass = binding.editTextPassword.text.toString()
                val loginRequest = LoginRequest(email, pass)
                loginAccount(loginRequest)
            }
        }
        binding.buttonCreateAccount.setOnClickListener {
            gotoCreateAccountActivity()
        }
    }

    private fun validateForm(): Boolean {
        return if (binding.editTextEmail.text.toString().isEmpty()) {
            showToast(getString(R.string.msj_empty_email))
            binding.editTextEmail.requestFocus()
            false
        } else if (binding.editTextPassword.text.toString().isEmpty()) {
            showToast(getString(R.string.msj_empty_password))
            binding.editTextPassword.requestFocus()
            false
        } else {
            true
        }
    }

    private fun loginAccount(loginRequest: LoginRequest) {
        apiService.login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                // Manejar respuesta
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {

                    if (response.code() == 200) {
                        sharedPref.savePref("jwt", response.body()?.jwt.toString())
                        getUserdata()
                    } else {
                        showToast("Error de Autenticacion")
                        activateButtonLogin(true)
                    }

                    Log.d("getIdAccountonResponse", "onResponse: ${response.body()?.jwt}")
                    Log.d("getIdAccountonResponse", "onResponse: ${response.body()}")
                    Log.d("getIdAccountonResponse", "onResponse: $response")
                    Log.d("getIdAccountonResponse", "onResponse: ${response.message()}")
                    Log.d("getIdAccountonResponse", "onResponse: ${response.headers()}")
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("getIdAccountonFailure", "onResponse: $t")
                    activateButtonLogin(true)
                }
            })
    }

    private fun gotoMainActivity() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun getUserdata() {
        val jwt = sharedPref.getPref("jwt", "").toString()
        apiService.getUserdata("jwt=$jwt")
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        savePrefers(response.body())
                        gotoMainActivity()
                    } else {
                        showToast("Error de Autenticacion")
                        activateButtonLogin(true)
                    }
                    Log.d("getIdAccountonResponse", "onResponse: ${response.body()}")
                    Log.d("getIdAccountonResponse", "onResponse: ${response.headers()}")
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("getIdAccountonResponse", "Error en la llamada a la API", t)
                    showToast("Error en la llamada a la API")
                }
            })
    }

    fun savePrefers(userdata: UserResponse?) {
        sharedPref.savePref("id_user", userdata?.id_user.toString())
        sharedPref.savePref("image_user", userdata?.image_user.toString())
        sharedPref.savePref("name_user", userdata?.name_user.toString())
        sharedPref.savePref("lastname_user", userdata?.lastname_user.toString())
        sharedPref.savePref("phone_user", userdata?.phone_user.toString())
        sharedPref.savePref("ismander_user", userdata?.ismander_user to Boolean)
    }


    private fun activateButtonLogin(b: Boolean) {
        binding.buttonLogin.isEnabled = b
    }


    private fun gotoCreateAccountActivity() {
        val i = Intent(this, CreateAccountActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
