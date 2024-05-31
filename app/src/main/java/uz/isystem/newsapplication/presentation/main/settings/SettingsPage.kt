package uz.isystem.newsapplication.presentation.main.settings

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import uz.isystem.newsapplication.databinding.PageSettingsBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.MainScreenDirections
import uz.isystem.newsapplication.presentation.main.settings.dialog.ChooseLanguageBottomSheet
import uz.isystem.newsapplication.presentation.main.settings.dialog.ChooseThemeBottomSheet

class SettingsPage : BaseFragment(R.layout.page_settings) {
    private lateinit var auth: FirebaseAuth
    private val binding by viewBinding(PageSettingsBinding::bind)
    private lateinit var themeBottomSheet: ChooseThemeBottomSheet
    private lateinit var langBottomSheet: ChooseLanguageBottomSheet
    private lateinit var cache : LocaleStorage
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        cache = LocaleStorage.getObject()
        themeBottomSheet = ChooseThemeBottomSheet()
        langBottomSheet = ChooseLanguageBottomSheet()
        auth = FirebaseAuth.getInstance()
        checkCache()
        listenActions()

    }

    private fun checkCache() {
        when(cache.getTheme()){
            0->binding.themeTitle.text = getString(R.string.title_system)
            1->binding.themeTitle.text = getString(R.string.title_dark)
            2->binding.themeTitle.text = getString(R.string.title_white)
        }
        when(cache.getLanguage()){
            "ru"->binding.languageTytle.text = getString(R.string.ru)
            "eng"->binding.languageTytle.text = getString(R.string.eng)
            "system"->binding.languageTytle.text = getString(R.string.system)
        }
    }

    private fun listenActions() {
        binding.profileBtn.setOnClickListener {
            nextScreen(MainScreenDirections.actionMainScreenToProfileScreen())
        }
        binding.logoutBtn.setOnClickListener {
            showDialog()
        }
        binding.notificationBtn.setOnClickListener {
            binding.switchNotification.isChecked = (!binding.switchNotification.isChecked)
        }

        binding.themeBtn.setOnClickListener {
            themeBottomSheet.show(childFragmentManager, "ThemeBottomSheet")
        }
        binding.languageBtn.setOnClickListener {
            langBottomSheet.show(childFragmentManager, "LanguageBottomSheet")
        }
    }

    private fun showDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        alertDialogBuilder.setTitle(getString(R.string.app_name))
        alertDialogBuilder.setMessage(getString(R.string.want_logout))

        alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            auth.signOut()
            LocaleStorage.getObject().setIsSigned(false)
            nextScreen(MainScreenDirections.actionMainScreenToLoginScreen())
            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
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