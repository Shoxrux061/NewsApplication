package uz.isystem.newsapplication.presentation.main.home.category.sports

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.presentation.main.MainScreenDirections

import uz.isystem.newsapplication.databinding.FragmentSportsBinding
import uz.isystem.newsapplication.presentation.adapter.CategoryAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.home.category.CategoryViewModel

class SportsFragment : BaseFragment(R.layout.fragment_sports){
    private val binding by viewBinding(FragmentSportsBinding::bind)
    private val viewModel : CategoryViewModel by viewModels()
    private val adapter by lazy{ CategoryAdapter(requireContext()) }
    private var isFirst = true

    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        if(isFirst){
            viewModel.getCategories(getString(R.string.language), "sports")
        }
        listenActions()
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

    private fun listenActions() {
        adapter.onClickItem={
            nextScreen(
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

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }
    private fun setShimmer() {
        binding.shimmer.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun nextScreen(navDirections: NavDirections) {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in)
            .setExitAnim(R.anim.slide_out)
            .setPopEnterAnim(R.anim.slide_in_reverse)
            .setPopExitAnim(R.anim.slide_out_reverse)
            .build()
        findNavController().navigate(navDirections, navOptions)
    }
}
