package uz.isystem.newsapplication.presentation.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.PageSearchBinding
import uz.isystem.newsapplication.presentation.NewsViewModel
import uz.isystem.newsapplication.presentation.adapter.CategoryAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.MainScreenDirections

class SearchPage : BaseFragment(R.layout.page_search) {
    private val binding by viewBinding(PageSearchBinding::bind)
    private val viewModel: NewsViewModel by viewModels()
    private val adapter = CategoryAdapter()
    private var isLoading = false
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        setAdapter()
        observe()
        setActions()
    }

    private fun setActions() {
        binding.search.setOnClickListener {
            adapter.clearData()
            if(!isLoading) {
                search(binding.searchEdt.text.toString())
            }
        }
        adapter.onClickItem={
            findNavController().navigate(SearchPageDirections.actionSearchPageToDetailsScreen(
                url = it.url?:"",
                title = it.title?:"no info",
                description = it.description?:"no info",
                author = it.author?:"unknown",
                imageUrl = it.urlToImage?:"",
                publishedAt = it.publishedAt
            ))
        }
    }

    private fun observe() {
        viewModel.successResponseEvery.observe(viewLifecycleOwner){
            adapter.setData(it!!.articles)
            isLoading = false
            hideLoading()
        }
        viewModel.errorResponseEvery.observe(viewLifecycleOwner){
            isLoading = false
            hideLoading()
        }
    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }

    private fun search(text: String) {
        if (text.isNotBlank() || text.isNotEmpty()) {
            viewModel.getEverything(q = text, lang = getString(R.string.language))
            showLoading()
            isLoading = true
        }else{
            binding.searchEdt.error = getString(R.string.emptyEdt)
        }
    }
    private fun hideLoading(){
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.isClickable = true
    }
    private fun showLoading(){
        binding.progressBar.visibility = View.VISIBLE
        binding.search.isClickable = false
    }
}
