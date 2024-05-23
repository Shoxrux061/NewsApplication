package uz.isystem.newsapplication.presentation.main.home.category.technology

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.FragmentTechnologyBinding
import uz.isystem.newsapplication.presentation.adapter.CategoryAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.home.category.CategoryViewModel

class TechnologyFragment : BaseFragment(R.layout.fragment_technology){
    private val binding by viewBinding(FragmentTechnologyBinding::bind)
    private val viewModel : CategoryViewModel by viewModels()
    private val adapter by lazy{ CategoryAdapter() }
    private var isFirst = true

    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        if(isFirst){
            viewModel.getCategories(getString(R.string.language), "technology")
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
