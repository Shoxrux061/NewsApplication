package uz.isystem.newsapplication.presentation.main.settings.dialog

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.N)
class ChooseLanguageBottomSheet : BottomSheetDialogFragment() {
    private lateinit var langRadioGroup: RadioGroup
    private lateinit var confirmButton: AppCompatButton
    private lateinit var cache: LocaleStorage
    private lateinit var engBtn: RadioButton
    private lateinit var ruBtn: RadioButton
    private lateinit var systemBtn: RadioButton


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_lang, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cache = LocaleStorage.getObject()
        langRadioGroup = view.findViewById(R.id.radio_group_lang)
        confirmButton = view.findViewById(R.id.confirm_button_lang)
        engBtn = view.findViewById(R.id.rd_eng)
        ruBtn = view.findViewById(R.id.rd_ru)
        systemBtn = view.findViewById(R.id.rd_system)

        checkLang()
        listenActions()

    }

    private fun checkLang() {
        when (cache.getLanguage()) {
            "ru" -> ruBtn.isChecked = true
            "eng" -> engBtn.isChecked = true
            "system" -> systemBtn.isChecked = true
        }
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
            cache.setLanguage(lang)
            if (lang == Locale.getDefault().language) {
                it.visibility = View.GONE
                return@setOnClickListener
            }
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

            it.visibility = View.GONE
        }
    }
}