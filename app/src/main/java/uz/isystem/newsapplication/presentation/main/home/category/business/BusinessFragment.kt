package uz.isystem.newsapplication.presentation.main.home.category.business

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.FragmentBusinessBinding
import uz.isystem.newsapplication.presentation.adapter.CategoryAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.home.category.CategoryViewModel

class BusinessFragment : BaseFragment(R.layout.fragment_business){

    private val binding by viewBinding(FragmentBusinessBinding::bind)
    private val viewModel : CategoryViewModel by viewModels()
    private val adapter by lazy{CategoryAdapter()}
    private var isFirst = true

    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        if(isFirst){
            viewModel.getCategories(getString(R.string.language), "business")
        }
        setAdapter()
        observe()

        isFirst = false
    }

    private fun setShimmer() {
        binding.shimmer.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
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
}