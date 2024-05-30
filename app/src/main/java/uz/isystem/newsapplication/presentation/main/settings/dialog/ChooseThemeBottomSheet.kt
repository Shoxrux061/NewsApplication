package uz.isystem.newsapplication.presentation.main.settings.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage

class ChooseThemeBottomSheet : BottomSheetDialogFragment() {

    private lateinit var radioGroup: RadioGroup
    private var isFirst = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_choose_theme, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        radioGroup = view.findViewById(R.id.theme_radio_group)

        if(isFirst){
            actionsListener()
        }
        isFirst = false

    }

    private fun actionsListener() {
        val cache = LocaleStorage.getObject()
        val currentTheme = cache.getTheme()

        var isChangingTheme = false

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (!isChangingTheme) {
                isChangingTheme = true
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
                isChangingTheme = false
            }
        }
    }
}