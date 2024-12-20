package uz.isystem.newsapplication.presentation.main.home.category.business

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.FragmentBusinessBinding
import uz.isystem.newsapplication.presentation.adapter.CategoryAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.extations.changeScreen
import uz.isystem.newsapplication.presentation.main.MainScreenDirections
import uz.isystem.newsapplication.presentation.main.home.category.CategoryViewModel

class BusinessFragment : BaseFragment(R.layout.fragment_business) {

    private val binding by viewBinding(FragmentBusinessBinding::bind)
    private val viewModel: CategoryViewModel by viewModels()
    private val adapter by lazy { CategoryAdapter(requireContext()) }
    private var isFirst = true

    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        if (isFirst) {
            viewModel.getCategories(getString(R.string.language), "business")
        }
        listenActions()
        setAdapter()
        observe()

        isFirst = false
    }

    private fun listenActions() {
        adapter.onClickItem = {
            findNavController().changeScreen(
                MainScreenDirections.actionMainScreenToDetailsScreen(
                    title = it.title.toString(),
                    publishedAt = it.publishedAt,
                    author = it.author.toString(),
                    imageUrl = it.urlToImage.toString(),
                    url = it.url.toString(),
                    description = it.description.toString(),
                    content = it.content.toString()
                )
            )
        }
    }

    private fun setShimmer() {
        binding.shimmer.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun observe() {
        viewModel.successResponseEvery.observe(viewLifecycleOwner) {
            setShimmer()
            adapter.setData(it!!.articles)
        }
    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }

}