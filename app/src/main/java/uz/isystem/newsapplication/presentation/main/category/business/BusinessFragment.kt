package uz.isystem.newsapplication.presentation.main.category.business

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.FragmentBusinessBinding
import uz.isystem.newsapplication.presentation.adapter.CategoryAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.MainScreenDirections
import uz.isystem.newsapplication.presentation.main.category.CategoryViewModel

class BusinessFragment : BaseFragment(R.layout.fragment_business){
    private val binding by viewBinding(FragmentBusinessBinding::bind)
    private val viewModel:CategoryViewModel by viewModels()
    private val adapter = CategoryAdapter()
    private var fragmentExecute = false
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        request()
        fragmentExecute = true
        setAdapter()
        observe()
        setActions()
    }

    private fun request() {
        if(!fragmentExecute) {
            viewModel.getCategories(lang = getString(R.string.language), category = "business")
        }
    }

    private fun setActions() {
        adapter.onClickItem={
            findNavController().navigate(MainScreenDirections.actionMainScreenToDetailsScreen(
                title = it.title?:"no info",
                imageUrl = it.urlToImage?:"",
                publishedAt = it.publishedAt,
                url = it.url?:"",
                description = it.description?:"no info",
                author = it.author?:"unknown"
            ))
        }
    }

    private fun observe() {
        viewModel.successResponseEvery.observe(viewLifecycleOwner){
            adapter.setData(it!!.articles)
        }
        viewModel.successResponseEvery.observe(viewLifecycleOwner){

        }
    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }
}
