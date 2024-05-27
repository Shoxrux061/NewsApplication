package uz.isystem.newsapplication.data.cache

import android.content.Context
import android.content.SharedPreferences

class LocaleStorage private constructor(context: Context) {

    private val keyIsSigned = "KEY_IS_SIGNED"
    private val keyEmail = "KEY_EMAIL"
    private val keyPassword = "KEY_PASSWORD"

    init {
        sharedPreferences = context.getSharedPreferences("cache", Context.MODE_PRIVATE)
    }

    companion object {
        private var appCache: LocaleStorage? = null
        private var sharedPreferences: SharedPreferences? = null

        fun init(context: Context) {
            if (appCache == null) {
                appCache = LocaleStorage(context)
            }
        }

        fun getObject(): LocaleStorage {
            return appCache!!
        }
    }

    fun setIsSigned(isSigned: Boolean) {
        if(!isSigned){
            sharedPreferences!!.edit().remove(keyPassword).apply()
            sharedPreferences!!.edit().remove(keyEmail).apply()
        }
        sharedPreferences!!.edit().putBoolean(keyIsSigned, isSigned).apply()
    }

    fun getIsSigned(): Boolean {
        return sharedPreferences!!.getBoolean(keyIsSigned, false)
    }

    fun setEmailNPassword(email: String, password: String) {
        sharedPreferences!!.edit().putString(keyEmail, email).apply()
        sharedPreferences!!.edit().putString(keyPassword, password).apply()
    }
    fun getEmail():String{
        return sharedPreferences!!.getString(keyEmail,"")!!
    }
    fun getPassword():String{
        return sharedPreferences!!.getString(keyPassword,"")!!
    }
}