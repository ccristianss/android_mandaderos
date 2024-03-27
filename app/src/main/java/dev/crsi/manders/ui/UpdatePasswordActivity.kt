package dev.crsi.manders.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.R
import dev.crsi.manders.databinding.ActivityUpdatePasswordBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.AccountPasswordChangeRequest
import dev.crsi.manders.models.AccountResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdatePasswordBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdatePasswordBinding.inflate(LayoutInflater.from(this))
        sharedPref = SharedPreferenceManager(this)
        setContentView(binding.root)


        initUI()
    }

    private fun initUI() {
        setListener()
    }

    private fun validatePassword(): Boolean {
        val oldpass = binding.editTextCurrentPassword.text.toString()
        val newpass = binding.editTextNewPassword.text.toString()
        val confirmpass = binding.editTextConfirmPassword.text.toString()
        val password = sharedPref.getPref("password_account", 0) as String

        return if (oldpass != password) {

            Toast.makeText(this, getString(R.string.msj_error_old_password), Toast.LENGTH_SHORT)
                .show()
            binding.editTextCurrentPassword.requestFocus()
            false

        } else if (newpass.length < 8) {
            Toast.makeText(this, getString(R.string.msj_min_length), Toast.LENGTH_SHORT).show()
            binding.editTextNewPassword.requestFocus()
            false
        } else if (confirmpass.length < 8) {
            Toast.makeText(this, getString(R.string.msj_min_length), Toast.LENGTH_SHORT).show()
            binding.editTextConfirmPassword.requestFocus()
            false
        } else if (newpass != confirmpass) {
            Toast.makeText(this, getString(R.string.msj_error_password), Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }


    private fun setListener() {
        binding.buttonChangePassword.setOnClickListener {

            if (validatePassword()) {

                val accountPasswordChangeRequest =
                    AccountPasswordChangeRequest(binding.editTextNewPassword.text.toString())
                accountPasswordChange(accountPasswordChangeRequest)
            }
        }
    }

    private fun accountPasswordChange(accountPasswordChangeRequest: AccountPasswordChangeRequest) {
        val id_account = sharedPref.getPref("id_account", 0) as Int

        apiService.changePasswordAccount(id_account, accountPasswordChangeRequest)
            .enqueue(object : Callback<AccountResponse> {
                override fun onResponse(
                    call: Call<AccountResponse>,
                    response: Response<AccountResponse>
                ) {
                    if (response.code() == 200) {

                        sharedPref.savePref(
                            "password_account",
                            accountPasswordChangeRequest.password_account
                        )
                        finish()

                    } else {
                        Toast.makeText(
                            this@UpdatePasswordActivity,
                            getString(R.string.msj_error_change_password),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<AccountResponse>, t: Throwable) {

                    Log.d("PasswordChangeFail", "onResponse: $t")
                }

            })

    }
}