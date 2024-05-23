package uz.isystem.newsapplication.presentation.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.ScreenSearchBinding
import uz.isystem.newsapplication.presentation.NewsViewModel
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.seeAll.SeeAllAdapter

class SearchScreen : BaseFragment(R.layout.screen_search) {
    private val binding by viewBinding(ScreenSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModels()
    private val adapter = SeeAllAdapter()
    private var isLoading = false
    private lateinit var lang: String
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        lang = getString(R.string.language)
        setAdapter()
        setActions()
        observe()
    }

    private fun setActions() {
        binding.searchEdt.addTextChangedListener {
            if (!isLoading) {
                showLoading()
                search(binding.searchEdt.text.toString())
            }
        }
    }

    private fun observe() {
        viewModel.successResponseEvery.observe(viewLifecycleOwner) {
            adapter.setData(it!!.articles)
            isLoading = false
            hideLoading()
        }
        viewModel.errorResponseEvery.observe(viewLifecycleOwner) {
            isLoading = false
            hideLoading()
        }
    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }

    private fun search(text: String) {
        if (text.isNotBlank()) {
            viewModel.search(q = text, lang = getString(R.string.language), sortBy = "", searchIn = "", from = "", to = "")
            showLoading()
            isLoading = true
        } else {
            binding.searchEdt.error = getString(R.string.emptyEdt)
        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.search.isClickable = true
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.search.isClickable = false
    }
}