package uz.isystem.newsapplication.data.cache

import android.content.Context
import android.content.SharedPreferences

class LocaleStorage private constructor(context: Context) {

    private val keyIsSigned = "KEY_IS_SIGNED"
    private val keyEmail = "KEY_EMAIL"
    private val keyPassword = "KEY_PASSWORD"
    private val keyIsFirst = "KEY_IS_FIRST"
    private val keyTheme = "KEY_THEME"
    private val keyLanguage = "KEY_LANGUAGE"

    init {
        sharedPreferences = context.getSharedPreferences("news_cache", Context.MODE_PRIVATE)
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
        if (!isSigned) {
            sharedPreferences!!.edit().remove(keyPassword).apply()
            sharedPreferences!!.edit().remove(keyEmail).apply()
        }
        sharedPreferences!!.edit().putBoolean(keyIsSigned, isSigned).apply()
    }

    /*
    fun clearAllData() {
        sharedPreferences?.edit()?.clear()?.apply()
    }*/

    fun setIsNotFirst() {
        sharedPreferences!!.edit().putBoolean(keyIsFirst, false).apply()
    }

    fun getIsFirst(): Boolean {
        return sharedPreferences!!.getBoolean(keyIsFirst, true)
    }

    fun getIsSigned(): Boolean {
        return sharedPreferences!!.getBoolean(keyIsSigned, false)
    }

    fun setEmailNPassword(email: String, password: String) {
        sharedPreferences!!.edit().putString(keyEmail, email).apply()
        sharedPreferences!!.edit().putString(keyPassword, password).apply()
    }

    fun getEmail(): String {
        return sharedPreferences!!.getString(keyEmail, "")!!
    }

    fun setTheme(theme: Int) {
        sharedPreferences!!.edit().putInt(keyTheme, theme).apply()
    }

    fun getTheme(): Int {
        return sharedPreferences!!.getInt(keyTheme, 0)
    }

    fun setLanguage(lang: String) {
        sharedPreferences!!.edit().putString(keyLanguage, lang).apply()
    }

    fun getLanguage(): String {
        return sharedPreferences!!.getString(keyLanguage, "system")!!
    }
}