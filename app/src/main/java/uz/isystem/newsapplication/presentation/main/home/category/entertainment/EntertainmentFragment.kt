package uz.isystem.newsapplication.presentation.main.home.category.entertainment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.FragmentBusinessBinding
import uz.isystem.newsapplication.databinding.FragmentEntertainmentBinding
import uz.isystem.newsapplication.presentation.adapter.CategoryAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.home.category.CategoryViewModel

class EntertainmentFragment : BaseFragment(R.layout.fragment_entertainment){
    private val binding by viewBinding(FragmentEntertainmentBinding::bind)
    private val viewModel : CategoryViewModel by viewModels()
    private val adapter by lazy{ CategoryAdapter() }
    private var isFirst = true

    override fun onCreate(view: View, savedInstanceState: Bundle?) {

        if(isFirst){
            viewModel.getCategories(getString(R.string.language), "entertainment")
        }
        setAdapter()
        observe()

        isFirst = false
    }

    private fun observe() {
        viewModel.successResponseEvery.observe(viewLifecycleOwner){
            setShimmer()
            adapter.setData(it!!.articles)
        }
    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }

    private fun setShimmer() {
        binding.shimmer.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }
}