package uz.isystem.newsapplication.presentation.main.settings.dialog

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat.recreate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.N)
class ChooseLanguageBottomSheet : BottomSheetDialogFragment() {
    private lateinit var langRadioGroup: RadioGroup
    private lateinit var confirmButton: AppCompatButton


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_lang, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        langRadioGroup = view.findViewById(R.id.radio_group_lang)
        confirmButton = view.findViewById(R.id.confirm_button_lang)


        listenActions()

    }

    private fun listenActions() {
        langRadioGroup.setOnCheckedChangeListener { _, checkedID ->

            when (checkedID) {
                R.id.rd_ru -> {
                    setLanguage("ru")
                }

                R.id.rd_eng -> {
                    setLanguage("eng")
                }

                R.id.rd_system -> {
                    setLanguage("system")
                }
            }

        }

    }

    private fun setLanguage(lang: String) {
        confirmButton.visibility = View.VISIBLE

        confirmButton.setOnClickListener {
            LocaleStorage.getObject().setLanguage(lang)
            val currentLocale = resources.configuration.locales.get(0)
            if (lang != "system") {
                val locale = Locale(lang)
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                resources.updateConfiguration(config, resources.displayMetrics)
                recreate(requireActivity())
            } else {
                val locale = Locale(currentLocale.language)
                Locale.setDefault(locale)
                val config = Configuration()
                config.setLocale(locale)
                resources.updateConfiguration(config, resources.displayMetrics)
                recreate(requireActivity())
            }
            it.visibility = View.GONE
        }
    }
}