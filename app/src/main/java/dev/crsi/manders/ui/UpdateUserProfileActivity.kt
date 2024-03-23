package dev.crsi.manders.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.R
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.UserRequest
import dev.crsi.manders.models.UserResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserProfileActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var buttonSave: Button
    private lateinit var sharedPref: SharedPreferenceManager
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user_profile)

        editTextName = findViewById(R.id.editTextName)
        editTextLastName = findViewById(R.id.editTextLastname)
        editTextPhone = findViewById(R.id.editTextPhone)
        buttonSave = findViewById(R.id.button)

        initUI()


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

        buttonSave.setOnClickListener {
            if (validateForm()) {
                val name_user = editTextName.text.toString()
                val lastname_user = editTextLastName.text.toString()
                val phone_user = editTextPhone.text.toString()
                val userRequest = UserRequest(name_user, lastname_user, phone_user)
                updateUserProfile(userRequest)
            }
        }
    }

    private fun updateUserProfile(userRequest: UserRequest) {
        Log.d("userRequest", "userRequest: $userRequest")

        apiService.createUser(userRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    savePrefs(response.body()?.id_user, userRequest.name_user, userRequest.lastname_user, userRequest.phone_user)
                    goToMain()
                } else {
                    Toast.makeText(
                        this@UpdateUserProfileActivity,
                        "Error al Guardar Usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Log.d("registerUserResponse", "onResponse: ${response.body()?.id_user}")
                Log.d("registerUserResponse", "onResponse: ${response.body()}")
                Log.d("registerUserResponse", "onResponse: $response")
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("registerUserFailure", "onFailure: $t")
            }
        })
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateForm(): Boolean {
        return when {
            editTextName.text.toString().isEmpty() -> {
                Toast.makeText(this, getString(R.string.msj_empty_name), Toast.LENGTH_SHORT).show()
                editTextName.requestFocus()
                false
            }
            editTextLastName.text.toString().isEmpty() -> {
                Toast.makeText(this, getString(R.string.msj_empty_lastname), Toast.LENGTH_SHORT).show()
                editTextLastName.requestFocus()
                false
            }
            editTextPhone.text.toString().isEmpty() -> {
                Toast.makeText(this, getString(R.string.msj_empty_phone), Toast.LENGTH_SHORT).show()
                editTextPhone.requestFocus()
                false
            }
            else -> true
        }
    }

    private fun savePrefs(idUser: Int?, name: String, lastName: String, phone: String) {
        val sharedPref = getSharedPreferences("nombre_de_tu_archivo_de_preferencias", MODE_PRIVATE)

        idUser?.let {
            sharedPref.edit().putInt("id_user", it).apply()
        }
        sharedPref.edit().putString("name_user", name).apply()
        sharedPref.edit().putString("lastname_user", lastName).apply()
        sharedPref.edit().putString("phone_user", phone).apply()
    }
}

