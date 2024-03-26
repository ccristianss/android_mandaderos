package dev.crsi.manders.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.R
import dev.crsi.manders.databinding.ActivityCreateUserProfileBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.UserRequest
import dev.crsi.manders.models.UserResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateUserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateUserProfileBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private var id_user = 0
    private var id_account = 0
    private var name = ""
    private var lastname = ""
    private var phone = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateUserProfileBinding.inflate(layoutInflater)
        sharedPref = SharedPreferenceManager(this)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        getPrefs()
        //getDataUsers()
        setListener()
    }

    private fun setListener() {
        binding.buttonSave.setOnClickListener {
            if (validateForm()) {
                activateButtonCreate(false)
                name = binding.editTextName.text.toString()
                lastname = binding.editTextLastName.text.toString()
                phone = binding.editTextPhone.text.toString()
                val userRequest = UserRequest(id_account, name, lastname, phone)
                registerUser(userRequest)
            }
        }
    }

    private fun registerUser(userRequest: UserRequest) {
        apiService.createUser(userRequest)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.code() == 201) {
                        savePrefers(response.body())
                        gotoMainActivity()

                    } else {
                        showToast("Error al Guardar Usuario")
                        activateButtonCreate(true)
                    }
                    Log.d("registerUserResponse", "onResponse: ${response.body()}")
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                    Log.d("registerUserFailure", "onResponse: $t")
                    activateButtonCreate(true)
                }
            })

    }

    private fun getPrefs() {
        id_account = sharedPref.getPref("id_account", 0) as Int
        Log.d("getPrefs", "id_account:$id_account")
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
                    }
                }
            }

            override fun onFailure(call: Call<List<UserResponse>>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun gotoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun validateForm(): Boolean {
        return if (binding.editTextName.text.toString().isEmpty()) {
            showToast(getString(R.string.msj_empty_name))
            binding.editTextName.requestFocus()
            false
        } else if (binding.editTextLastName.text.toString().isEmpty()) {
            showToast(getString(R.string.msj_empty_lastname))
            binding.editTextLastName.requestFocus()
            false
        } else if (binding.editTextPhone.text.toString().isEmpty()) {
            showToast(getString(R.string.msj_empty_phone))
            binding.editTextPhone.requestFocus()
            false
        } else {
            true
        }
    }

    fun savePrefers(userdata: UserResponse?) {
        sharedPref.savePref("id_user", userdata?.id_user.toString())
        sharedPref.savePref("image_user", userdata?.image_user.toString())
        sharedPref.savePref("name_user", userdata?.name_user.toString())
        sharedPref.savePref("lastname_user", userdata?.lastname_user.toString())
        sharedPref.savePref("phone_user", userdata?.phone_user.toString())
        sharedPref.savePref("ismander_user", userdata?.ismander_user to Boolean)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun activateButtonCreate(b: Boolean) {
        binding.buttonSave.isEnabled = b
    }
}