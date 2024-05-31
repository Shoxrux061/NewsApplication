package uz.isystem.newsapplication.presentation.main.settings.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage

class ChooseThemeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var radioGroup: RadioGroup
    private lateinit var cache: LocaleStorage
    private lateinit var darkTheme: RadioButton
    private lateinit var whiteTheme: RadioButton
    private lateinit var systemTheme: RadioButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_choose_theme, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cache = LocaleStorage.getObject()
        radioGroup = view.findViewById(R.id.theme_radio_group)
        darkTheme = view.findViewById(R.id.dark_theme)
        whiteTheme = view.findViewById(R.id.white_theme)
        systemTheme = view.findViewById(R.id.system_theme)


        when (cache.getTheme()) {
            0 -> systemTheme.isChecked = true
            1 -> darkTheme.isChecked = true
            2 -> whiteTheme.isChecked = true
        }

        actionsListener()

    }

    private fun actionsListener() {
        val currentTheme = cache.getTheme()


        radioGroup.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                R.id.dark_theme -> {
                    if (currentTheme != 1) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        cache.setTheme(1)
                    }
                }

                R.id.white_theme -> {
                    if (currentTheme != 2) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        cache.setTheme(2)
                    }
                }

                R.id.system_theme -> {
                    if (currentTheme != 0) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        cache.setTheme(0)
                    }
                }
            }
        }
    }
}