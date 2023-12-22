package uz.isystem.newsapplication.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.HomePageBinding
import uz.isystem.newsapplication.presentation.NewsViewModel
import uz.isystem.newsapplication.presentation.adapter.HomeAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.MainScreenDirections

class HomePage : BaseFragment(R.layout.home_page){
    private val binding by viewBinding(HomePageBinding::bind)
    private val adapter = HomeAdapter()
    private val viewModel:HomeViewModel by viewModels()
    private var isLoading = false
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        setAdapter()
        observe()
        setActions()
    }


    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }
    private fun setActions(){
        adapter.onClickItem = {
            findNavController().navigate(MainScreenDirections.actionMainScreenToDetailsScreen(
                description = it.description?:"no info",
                title = it.title?:"",
                imageUrl = it.urlToImage?:"",
                url = it.url?:"no info",
                publishedAt = it.publishedAt,
                author = it.author?:"unknown"
            ))
        }
        adapter.onPaginate = {
            if(!isLoading) {
                viewModel.getEverything(lang = getString(R.string.language))
                isLoading = true
            }
        }
    }

    private fun observe() {
        viewModel.successResponseEvery.observe(viewLifecycleOwner){
            adapter.setData(it!!.articles)
            isLoading = false
        }
        viewModel.errorResponseEvery.observe(viewLifecycleOwner){
            isLoading = false
        }
    }
}

