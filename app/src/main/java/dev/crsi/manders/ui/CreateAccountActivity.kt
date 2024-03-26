package dev.crsi.manders.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.R
import dev.crsi.manders.databinding.ActivityCreateAccountBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.AccountRequest
import dev.crsi.manders.models.AccountResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private var email = ""
    private var pass = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        sharedPref = SharedPreferenceManager(this)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        setListener()
    }

    private fun setListener() {
        binding.buttoncreateAccount.setOnClickListener {
            if (validateForm()) {
                email = binding.editTextEmail.text.toString()
                pass = binding.editTextPassword.text.toString()
                val accountRequest = AccountRequest(email, pass)
                registerAccount(accountRequest)
            }
        }

        binding.buttonLogin.setOnClickListener {
            gotoLoginActivity()
        }
    }

    private fun registerAccount(accountRequest: AccountRequest) {
        apiService.createAccount(accountRequest)
            .enqueue(object : Callback<AccountResponse> {
                override fun onResponse(
                    call: Call<AccountResponse>,
                    response: Response<AccountResponse>
                ) {

                    if (response.code() == 201) {
                        savePrefers(response.body())
                        gotoLoginActivity()

                    } else {
                        showToast("Error de Crear Cuenta")
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

    private fun savePrefers(accountdata: AccountResponse?) {
        sharedPref.savePref("id_account", accountdata?.id_account.toString())
        sharedPref.savePref("email_account", accountdata?.id_account.toString())
        sharedPref.savePref("password_account", pass)

    }

    private fun validateForm(): Boolean {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()
        val confirmPassword = binding.editTextConfirmPassword.text.toString()

        return when {
            email.isEmpty() -> {
                showToast(getString(R.string.msj_empty_email))
                binding.editTextEmail.requestFocus()
                false
            }

            password.isEmpty() -> {
                showToast(getString(R.string.msj_empty_password))
                binding.editTextPassword.requestFocus()
                false
            }

            confirmPassword.isEmpty() -> {
                showToast(getString(R.string.msj_empty_password))
                binding.editTextConfirmPassword.requestFocus()
                false
            }

            confirmPassword != password -> {
                showToast(getString(R.string.msj_error_password))
                false
            }

            else -> true
        }
    }

    private fun gotoLoginActivity() {
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}