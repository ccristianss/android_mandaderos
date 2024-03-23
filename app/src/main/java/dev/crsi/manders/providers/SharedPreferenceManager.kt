package dev.crsi.manders.providers

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager (context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("SP", Context.MODE_PRIVATE)
    }

    fun savePref(key: String, value: Any) {
        val editor = sharedPreferences.edit()
        when (value) {
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            else -> throw IllegalArgumentException("Este tipo de datos no puede ser guardado")
        }
        editor.apply()
    }

    fun getPref(key: String, defaultValue: Any): Any {
        return when(defaultValue){
            is String -> sharedPreferences.getString(key, defaultValue)
            is Int -> sharedPreferences.getInt(key, defaultValue)
            is Long-> sharedPreferences.getLong(key, defaultValue)
            is Boolean -> sharedPreferences.getBoolean(key, defaultValue)
            is Float -> sharedPreferences.getFloat(key, defaultValue)
            else -> throw  IllegalArgumentException("No es posible obtener este tipo de dato")
        }!!
    }

    fun removePref(key:String){
       val editor= sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun removeAll(){
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}