package uz.isystem.newsapplication.presentation.extations

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import uz.isystem.newsapplication.R

private var isNavigating = false

fun NavController.changeScreen(navDirections: NavDirections) {


    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in)
        .setExitAnim(R.anim.slide_out)
        .setPopUpTo(R.id.mainScreen, false)
        .setLaunchSingleTop(true)
        .setPopEnterAnim(R.anim.slide_in_reverse)
        .setPopExitAnim(R.anim.slide_out_reverse)
        .build()

    val currentDestinationId = currentDestination?.id
    val actionId = navDirections.actionId

    try {
        if (currentDestinationId != null && currentDestinationId == actionId) {
            Log.d("Navigation", "Navigation ignored: Target destination is already active.")
            return
        }

        if (isNavigating) return
        isNavigating = true
        navigate(navDirections, navOptions)
        Handler(Looper.getMainLooper()).postDelayed({ isNavigating = false }, 500)

    } catch (e: IllegalArgumentException) {
        Log.e("Navigation", "Navigation failed: ${e.message}")
    }
}


