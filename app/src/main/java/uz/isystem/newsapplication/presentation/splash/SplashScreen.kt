package uz.isystem.newsapplication.presentation.splash

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import uz.isystem.newsapplication.presentation.base.BaseFragment
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.N)
@SuppressLint("CustomSplashScreen")
class SplashScreen : BaseFragment(R.layout.screen_splash) {
    private lateinit var cache: LocaleStorage
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

        cache = LocaleStorage.getObject()

        checkIsFirst()
        setLanguage()
        checkCache()

    }

    private fun setLanguage() {
        val currentLanguage = Locale.getDefault().language
        Log.d("TAGLang", "setLanguage: $currentLanguage")
        if (cache.getLanguage() != "system" && cache.getLanguage() != currentLanguage) {
            val locale = Locale(cache.getLanguage())
            Locale.setDefault(locale)
            val config = Configuration()
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
            ActivityCompat.recreate(requireActivity())
        }
    }

    private fun checkCache() {

        when (cache.getTheme()) {
            0 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            1 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            2 -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        when (cache.getLanguage()) {
            "ru" -> setLanguage("ru")
            "eng" -> setLanguage("eng")
            "system" -> setLanguage("system")
        }
    }

    private fun setLanguage(lang: String) {

        cache.setLanguage(lang)
        val newLocale = if (lang == "system") {
            Resources.getSystem().configuration.locales.get(0)
        } else {
            Locale(lang)
        }

        Locale.setDefault(newLocale)
        val config = Configuration()
        config.setLocale(newLocale)

        requireActivity().resources.updateConfiguration(
            config,
            requireActivity().resources.displayMetrics
        )

        requireActivity().recreate()

    }

    private fun checkIsFirst() {
        if (cache.getIsFirst()) {
            nextScreen(SplashScreenDirections.actionSplashScreenToOnBoardScreen())
        } else if (cache.getIsSigned()) {
            nextScreen(SplashScreenDirections.actionSplashScreenToMainScreen())
        } else {
            nextScreen(SplashScreenDirections.actionSplashScreenToLoginScreen())
        }
    }

    private fun nextScreen(navDirections: NavDirections) {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in)
            .setExitAnim(R.anim.slide_out)
            .setPopEnterAnim(R.anim.slide_in_reverse)
            .setPopExitAnim(R.anim.slide_out_reverse)
            .build()
        findNavController().navigate(navDirections, navOptions)
    }

}