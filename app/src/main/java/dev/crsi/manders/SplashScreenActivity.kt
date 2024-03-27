package dev.crsi.manders

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.databinding.ActivitySplashScreenBinding
import dev.crsi.manders.interfaces.ApiService
import dev.crsi.manders.models.UserResponse
import dev.crsi.manders.providers.ApiClient
import dev.crsi.manders.providers.SharedPreferenceManager
import dev.crsi.manders.ui.LoginActivity
import dev.crsi.manders.ui.MainActivity
import dev.crsi.manders.ui.MainManderActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)
    private val timeSplash = 3600L
    private var id_account = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(LayoutInflater.from(this))
        sharedPref = SharedPreferenceManager(this)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        getPrefs()
        if (sharedPref.getPref("jwt", "").toString().isNotEmpty()) {
            getUserdata()
        } else {
            gotoLoginAcivity()
        }
    }

    private fun getPrefs() {
        id_account = sharedPref.getPref("id_account", 0) as Int
        Log.d("getPrefs", "id_account:$id_account")
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
                        gotoNextActivity()
                    } else {
                        showToast("Error de Autenticacion")
                        gotoLoginAcivity()
                    }
                    Log.d("getIdAccountonResponse", "onResponse: ${response.body()}")
                    Log.d("getIdAccountonResponse", "onResponse: ${response.headers()}")
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("getIdAccountonResponse", "Error en la llamada a la API", t)
                    showToast("Error en la llamada a la API")
                    gotoLoginAcivity()
                }
            })
    }

    private fun gotoLoginAcivity() {
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun gotoNextActivity() {
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                if (sharedPref.getPref("ismander_user", false) as Boolean) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }, timeSplash)
        }
    }

    fun savePrefers(userdata: UserResponse?) {
        sharedPref.savePref("id_user", userdata?.id_user as Int)
        sharedPref.savePref("id_account", userdata.account_id_account)
        sharedPref.savePref("image_user", userdata.image_user.toString())
        sharedPref.savePref("name_user", userdata.name_user)
        sharedPref.savePref("lastname_user", userdata.lastname_user)
        sharedPref.savePref("phone_user", userdata.phone_user)
        sharedPref.savePref("ismander_user", userdata.ismander_user)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}