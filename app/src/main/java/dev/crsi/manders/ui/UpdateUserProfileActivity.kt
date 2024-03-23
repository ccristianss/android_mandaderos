package dev.crsi.manders.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.R
import dev.crsi.manders.databinding.ActivityUpdateUserProfileBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.UserRequest
import dev.crsi.manders.models.UserResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUserProfileBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private var id_user = 0
    private var id_account = 0
    private var name = ""
    private var lastname = ""
    private var phone = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserProfileBinding.inflate(LayoutInflater.from(this))
        sharedPref = SharedPreferenceManager(this)
        setContentView(binding.root)

        initUI()


    }

    private fun initUI() {
        getPrefs()
        getDataUsers()
        setListener()
    }

    private fun getDataUsers() {
        apiService.getUsers().enqueue(object : Callback<List<UserResponse>> {
            override fun onResponse(
                call: Call<List<UserResponse>>,
                response: Response<List<UserResponse>>
            ) {
                if (response.isSuccessful) {
                    val usersList = response.body()!!
                    val filteredUsers = usersList.filter { it.account_id_account == id_account }

                    filteredUsers.forEach { user ->
                        name = user.name_user
                        lastname = user.lastname_user
                        phone = user.phone_user
                        savePrefers(user.id_user)
                    }
                    getPrefs()
                    setData()
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {

                Toast.makeText(
                    this@UpdateUserProfileActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun setData() {
        binding.editTextPhone.setText(phone)
        binding.editTextLastname.setText(lastname)
        binding.editTextName.setText(name)
    }

    private fun setListener() {
        binding.button.setOnClickListener {
            if (validateForm()) {
                val name_user = binding.editTextName.text.toString()
                val lastname_user = binding.editTextLastname.text.toString()
                val phone_user = binding.editTextPhone.text.toString()
                val userRequest = UserRequest(id_account, name_user, lastname_user, phone_user)
                updateUserProfile(userRequest)


            }
        }
    }

    private fun getPrefs() {
        id_account = sharedPref.getPref("id_account", 0) as Int
        Log.d("getPrefs", "id_account:$id_account")
        id_user = sharedPref.getPref("id_user", 0) as Int
        Log.d("getPrefs", "id_user:$id_user")
    }

    private fun updateUserProfile(userRequest: UserRequest) {
        Log.d("userRequest", "userRequest: $userRequest")

        apiService.updateUser(id_user, userRequest).enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    savePrefers(response.body()?.id_user)
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
        return if (binding.editTextName.text.toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.msj_empty_name), Toast.LENGTH_SHORT).show()
            binding.editTextName.requestFocus()
            false
        } else if (binding.editTextLastname.text.toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.msj_empty_lastname), Toast.LENGTH_SHORT).show()
            binding.editTextLastname.requestFocus()
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

