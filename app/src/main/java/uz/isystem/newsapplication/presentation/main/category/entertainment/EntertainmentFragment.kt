package uz.isystem.newsapplication.presentation.main.category.entertainment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.FragmentEntertainmentBinding
import uz.isystem.newsapplication.presentation.adapter.CategoryAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.MainScreenDirections
import uz.isystem.newsapplication.presentation.main.category.CategoryViewModel

class EntertainmentFragment : BaseFragment(R.layout.fragment_entertainment){
    private val binding by viewBinding(FragmentEntertainmentBinding::bind)
    private val viewModel: CategoryViewModel by viewModels()
    private val adapter = CategoryAdapter()
    private var fragmentExecute = false
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        if(!fragmentExecute) {
            viewModel.getCategories(lang = getString(R.string.language), category = "entertainment")
        }
        fragmentExecute = true
        setAdapter()
        observe()
        setActions()
    }

    private fun setActions() {
        adapter.onClickItem={
            findNavController().navigate(
                MainScreenDirections.actionMainScreenToDetailsScreen(
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


