package uz.isystem.newsapplication.presentation.search.filtr

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.vicmikhailau.maskededittext.MaskedEditText
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.model.FilterModel
import uz.isystem.newsapplication.databinding.ScreenFilterBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)

class FilterScreen : BaseFragment(R.layout.screen_filter) {

    private val binding by viewBinding(ScreenFilterBinding::bind)
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

        setSpinnerData()
        listenActions()

    }

    private fun listenActions() {
        binding.searchBtn.setOnClickListener {
            var searchIn = ""
            var sortBy = ""
            if (checkValid()) {
                when (binding.radioGroup.checkedRadioButtonId) {
                    R.id.in_title -> {
                        searchIn = "title"
                    }

                    R.id.in_description -> {
                        searchIn = "description"
                    }

                    R.id.in_content -> {
                        searchIn = "content"
                    }
                }
                when (binding.spinner.selectedItem) {
                    R.string.by_relevance -> {
                        sortBy = getString(R.string.key_relevance)
                    }

                    R.string.by_date -> {
                        sortBy = getString(R.string.date_key)
                    }

                    R.string.by_popularity -> {
                        sortBy = getString(R.string.popularity_key)
                    }
                }
                binding.spinner.selectedItem
                val filterData = FilterModel(
                    searchIn = searchIn,
                    sortBy = sortBy,
                    q = binding.searchEdt.text.toString(),
                    from = binding.from.text.toString(),
                    to = binding.to.toString(),
                    isBack = true
                )
                nextScreen(FilterScreenDirections.actionFilterScreenToSearchPage(filterData))
            }
        }
        binding.fromDatePicker.setOnClickListener {
            showDatePickerDialog(binding.from)
        }
        binding.toDatePicker.setOnClickListener {
            showDatePickerDialog(binding.to)
        }
    }

    private fun checkValid(): Boolean {
        val dateText1 = binding.from.text.toString()
        val dateText2 = binding.to.text.toString()

        if (!isValidDate(dateText1) || !isValidDate(dateText2)) {
            Toast.makeText(context, "Неправильная дата", Toast.LENGTH_SHORT).show()
            return false
        } else if (compareDates(dateText1, dateText2) >= 0) {
            Toast.makeText(context, "Идентичные даты", Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.searchEdt.text.isBlank()) {
            binding.searchEdt.error = getString(R.string.inputError)
            return false
        }
        return true
    }

    private fun showDatePickerDialog(view: MaskedEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val finalMonth = if (selectedMonth <= 10) {
                    "0${selectedMonth + 1}"
                } else {
                    (selectedMonth + 1).toString()
                }
                val finalDay = if (selectedDay < 10) {
                    "0$selectedDay"
                } else {
                    selectedDay.toString()
                }
                val selectedDate = "$selectedYear-$finalMonth-$finalDay"
                view.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun isValidDate(dateString: String): Boolean {

        if (dateString.isEmpty()) {
            return true
        }
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            LocalDate.parse(dateString, formatter)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }

    private fun compareDates(dateString1: String, dateString2: String): Int {

        if (dateString2.isBlank() && dateString1.isBlank()) {
            return -1
        }

        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date1 = LocalDate.parse(dateString1, formatter)
            val date2 = LocalDate.parse(dateString2, formatter)
            date1.compareTo(date2)
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("One or both dates are invalid")
        }
    }

    private fun setSpinnerData() {
        val data = arrayOf(
            getString(R.string.by_relevance),
            getString(R.string.by_date),
            getString(R.string.by_popularity)
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, data)
        binding.spinner.adapter = adapter
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