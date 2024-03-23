package dev.crsi.manders

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import dev.crsi.manders.databinding.ActivitySplashScreenBinding
import dev.crsi.manders.providers.SharedPreferenceManager
import dev.crsi.manders.ui.LoginActivity
import dev.crsi.manders.ui.RegisterActivity
import dev.crsi.manders.ui.UpdateUserProfileActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var sharedPref: SharedPreferenceManager
    private val timeSplash = 3000L
    private var id_account = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(LayoutInflater.from(this))
        sharedPref = SharedPreferenceManager(this)
        setContentView(binding.root)

        initUI()

        //val intent = Intent(this, UpdateUserProfileActivity::class.java)
        //finish()
        //startActivity(intent)
    }

    private fun initUI() {
        getPrefs()
        setNextActivity()
    }

    private fun getPrefs() {
        id_account = sharedPref.getPref("id_account", 0) as Int
        Log.d("getPrefs", "id_account:$id_account")
    }

    private fun setNextActivity() {
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                if (id_account == 0) {
                    val intent = Intent(this, LoginActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                    finish()

                }
            }, timeSplash)
        }
    }
}