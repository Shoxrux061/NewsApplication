package uz.isystem.newsapplication.presentation.search

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.ScreenSearchBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.seeAll.SeeAllAdapter

class SearchScreen : BaseFragment(R.layout.screen_search) {

    private val binding by viewBinding(ScreenSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModels()
    private val adapter by lazy { SeeAllAdapter(requireContext()) }
    private var isLoading = false
    private val args: SearchScreenArgs by navArgs()
    private var enteredWord = ""
    private lateinit var lang: String
    private var sortBy = ""
    private var searchIn = ""
    private var from = ""
    private var to = ""
    private var q = ""


    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        lang = getString(R.string.language)
        onBackPressed()
        checkIsBack()
        setAdapter()
        setActions()
        observe()
    }

    private fun checkIsBack() {

        args.data?.let { data ->
            if (data.isBack) {
                binding.searchEdt.setText(data.q)
                searchIn = data.searchIn ?: ""
                sortBy = data.sortBy ?: ""
                from = data.from ?: ""
                to = data.to ?: ""
                q = data.q
                viewModel.search(
                    q = q,
                    searchIn = searchIn,
                    sortBy = sortBy,
                    from = from,
                    to = to,
                    lang = lang
                )
                isLoading = true
                showLoading()
            }
        }

    }

    private fun setActions() {
        binding.searchEdt.addTextChangedListener {
            enteredWord = binding.searchEdt.text.toString()
            if (binding.searchEdt.text.isNotBlank()) {
                adapter.clearData()
                isLoading = true
                showLoading()
                search(enteredWord)
            }
        }

        binding.filterBtn.setOnClickListener {
            nextScreen(SearchScreenDirections.actionSearchPageToFilterScreen())
        }

        adapter.onPaginate = {
            if (!isLoading) {
                isLoading = true
                binding.paginateProgress.visibility = View.VISIBLE
                viewModel.search(
                    q = q,
                    searchIn = searchIn,
                    sortBy = sortBy,
                    from = from,
                    lang = lang,
                    to = to
                )
            }
        }
        adapter.onClickItem = {
            nextScreen(
                SearchScreenDirections.actionSearchPageToDetailsScreen(
                    title = it.title!!,
                    url = it.url!!,
                    description = it.description!!,
                    imageUrl = it.urlToImage!!,
                    publishedAt = it.publishedAt,
                    author = it.author ?: "unknown",
                    content = it.content!!
                )
            )
        }
    }

    private fun observe() {
        viewModel.successResponseEvery.observe(viewLifecycleOwner) {
            adapter.setData(it!!.articles)
            isLoading = false
            hideLoading()
        }
        viewModel.errorResponseEvery.observe(viewLifecycleOwner) {
            isLoading = false
            hideLoading()
        }
    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }

    private fun search(text: String) {
        if (text.isNotBlank()) {
            viewModel.search(
                q = text,
                lang = lang,
                sortBy = sortBy,
                searchIn = searchIn,
                from = from,
                to = to
            )
            showLoading()
            isLoading = true
        } else {
            binding.searchEdt.error = getString(R.string.emptyEdt)
        }
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.filterBtn.isClickable = true
        binding.paginateProgress.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.filterBtn.isClickable = false
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

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    nextScreen(SearchScreenDirections.actionSearchPageToMainScreen())
                }
            })
    }
}