package uz.isystem.newsapplication.presentation.main.home.category.since

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.FragmentBusinessBinding
import uz.isystem.newsapplication.databinding.FragmentSinceBinding
import uz.isystem.newsapplication.presentation.adapter.CategoryAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.home.category.CategoryViewModel

class SinceFragment : BaseFragment(R.layout.fragment_since){
    private val binding by viewBinding(FragmentSinceBinding::bind)
    private val viewModel : CategoryViewModel by viewModels()
    private val adapter by lazy{ CategoryAdapter() }
    private var isFirst = true

    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        if(isFirst){
            viewModel.getCategories(getString(R.string.language), "science")
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
