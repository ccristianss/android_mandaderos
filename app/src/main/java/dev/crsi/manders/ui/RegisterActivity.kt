package dev.crsi.manders.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.R
import dev.crsi.manders.databinding.ActivityRegisterBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.UserRequest
import dev.crsi.manders.models.UserResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private var id_user = 0
    private var id_account = 0
    private var name = ""
    private var lastname = ""
    private var phone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        sharedPref = SharedPreferenceManager(this)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        getPrefs()
        setListener()
    }

    private fun getPrefs() {
        id_account = sharedPref.getPref("id_account", 0) as Int
        id_user = sharedPref.getPref("id_user", 0) as Int
        if (id_user != 0) {
            GotoMain()
        }
    }

    private fun setListener() {
        binding.buttonSave.setOnClickListener {
            if (validateForm()) {
                name = binding.editTextName.text.toString()
                lastname = binding.editTextLastName.text.toString()
                phone = binding.editTextPhone.text.toString()
                val userRequest = UserRequest(id_account, name, lastname, phone)
                registerUser(userRequest)
            }
        }

    }

    private fun registerUser(userRequest: UserRequest) {
        Log.d("userRequest", "userRequest: $userRequest")

        apiService.createUser(userRequest)
            .enqueue(object : Callback<UserResponse> {
                // Manejar respuesta
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {

                    if (response.code() == 201) {
                        savePrefers(response.body()?.id_user)
                        GotoMain()

                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error al Guardar Usuario",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    Log.d("registerUserResponse", "onResponse: ${response.body()?.id_user}")
                    Log.d("registerUserResponse", "onResponse: ${response.body()}")
                    Log.d("registerUserResponse", "onResponse: $response")
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                    Log.d("registerUserFailure", "onResponse: $t")
                }
            })

    }

    private fun GotoMain() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }


    private fun validateForm(): Boolean {
        return if (binding.editTextName.text.toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.msj_empty_name), Toast.LENGTH_SHORT).show()
            binding.editTextName.requestFocus()
            false
        } else if (binding.editTextLastName.text.toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.msj_empty_lastname), Toast.LENGTH_SHORT).show()
            binding.editTextLastName.requestFocus()
            false
        } else if (binding.editTextPhone.text.toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.msj_empty_phone), Toast.LENGTH_SHORT).show()
            binding.editTextPhone.requestFocus()
            false
        } else {
            true
        }
    }

    fun savePrefers(idUser: Int?) {
        if (idUser != null) {
            sharedPref.savePref("id_user", idUser)
        }
        sharedPref.savePref("name_user", name)
        sharedPref.savePref("lastname_user", lastname)
        sharedPref.savePref("phone_user", phone)
    }
}