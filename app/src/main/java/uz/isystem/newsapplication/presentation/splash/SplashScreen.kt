package uz.isystem.newsapplication.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import uz.isystem.newsapplication.databinding.ScreenSplashBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment

@SuppressLint("CustomSplashScreen")
class SplashScreen : BaseFragment(R.layout.screen_splash) {
    private val binding by viewBinding(ScreenSplashBinding::bind)
    private lateinit var cache: LocaleStorage
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        cache = LocaleStorage.getObject()
        checkIsFirst()

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