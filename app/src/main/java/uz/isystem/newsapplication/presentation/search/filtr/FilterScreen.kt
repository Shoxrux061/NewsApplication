package uz.isystem.newsapplication.presentation.search.filtr

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.ScreenFilterBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment

class FilterScreen : BaseFragment(R.layout.screen_filter) {

    private val binding by viewBinding(ScreenFilterBinding::bind)
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

        setSpinnerData()
        listenActions()


    }

    private fun listenActions() {
        binding.searchBtn.setOnClickListener {

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
}
