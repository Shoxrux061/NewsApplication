package uz.isystem.newsapplication.presentation.seeAll

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.ScreenSeeAllBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.extations.changeScreen

class SeeAllScreen : BaseFragment(R.layout.screen_see_all) {
    private val binding by viewBinding(ScreenSeeAllBinding::bind)
    private val viewModel: SeeAllViewModel by viewModels()
    private val adapter by lazy { SeeAllAdapter(requireContext()) }
    private val args: SeeAllScreenArgs by navArgs()
    private var isLoading = false
    private lateinit var lang: String
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        lang = getString(R.string.language)
        if (args.category != "top") {
            binding.category.text = args.category.capitalize()
        } else {
            binding.category.text = getString(R.string.trending).capitalize()
        }
        setAdapter()
        sendRequest()
        observe()
        listenActions()

    }

    private fun listenActions() {

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        adapter.onPaginate = {
            sendRequest()
        }
        adapter.onClickItem = {
            findNavController().changeScreen(
                SeeAllScreenDirections.actionSeeAllScreenToDetailsScreen(
                    title = it.title.toString(),
                    publishedAt = it.publishedAt,
                    author = it.author ?: "unknown",
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

    private fun observe() {
        viewModel.successResponseCategory.observe(viewLifecycleOwner) {
            adapter.setData(it!!.articles)
            isLoading = false
        }
        viewModel.successResponseEvery.observe(viewLifecycleOwner) {
            adapter.setData(it!!.articles)
            isLoading = false
        }
        viewModel.errorResponseEvery.observe(viewLifecycleOwner) {
            isLoading = false
        }
        viewModel.errorResponseCategory.observe(viewLifecycleOwner) {
            isLoading = false
        }
    }

    private fun sendRequest() {
        if (!isLoading) {
            if (args.category == "top") {
                viewModel.getEverything(lang)
            } else {
                viewModel.getCategories(lang, args.category)
            }
            isLoading = true
        }
    }

}