package uz.isystem.newsapplication.presentation.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.ScreenSearchBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.search.filtr.FilterDialog
import uz.isystem.newsapplication.presentation.seeAll.SeeAllAdapter

class SearchScreen : BaseFragment(R.layout.screen_search) {

    private val binding by viewBinding(ScreenSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModels()
    private var dialogFilter: FilterDialog? = null
    private val adapter by lazy { SeeAllAdapter(requireContext()) }
    private var isLoading = false
    private var enteredWord = ""
    private lateinit var lang: String
    private var sortBy = ""
    private var searchIn = ""
    private var from = ""
    private var to = ""


    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        dialogFilter = FilterDialog()
        lang = getString(R.string.language)
        setAdapter()
        setActions()
        observe()
    }


    private fun setActions() {

        dialogFilter?.onSearchClick = {
            searchIn = it.searchIn!!
            from = it.from ?: ""
            to = it.to ?: ""
            sortBy = it.sortBy ?: ""
            binding.searchEdt.setText(it.q)
            search(it.q)
        }

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
            dialogFilter!!.show(childFragmentManager, "FilterDialog")
        }

        adapter.onPaginate = {
            if (!isLoading) {
                isLoading = true
                binding.paginateProgress.visibility = View.VISIBLE
                viewModel.search(
                    q = binding.searchEdt.text.toString(),
                    searchIn = searchIn,
                    sortBy = sortBy,
                    from = from,
                    lang = lang,
                    to = to
                )
            }
        }
        adapter.onClickItem = {

            findNavController().navigate(
                SearchScreenDirections.actionSearchPageToDetailsScreen(
                    title = it.title!!,
                    url = it.url!!,
                    description = it.description!!,
                    imageUrl = it.urlToImage!!,
                    publishedAt = it.publishedAt,
                    author = it.author ?: "unknown",
                    content = it.content!!
                ),
                NavOptions.Builder()
                    .setPopUpTo(
                        R.id.searchPage,
                        false
                    ).setEnterAnim(R.anim.slide_in)
                    .setExitAnim(R.anim.slide_out)
                    .setPopEnterAnim(R.anim.slide_in_reverse)
                    .setPopExitAnim(R.anim.slide_out_reverse)
                    .build()
            )
        }
    }

    private fun observe() {
        viewModel.successResponseEvery.observe(viewLifecycleOwner) {
            adapter.setData(it!!.articles)
            isLoading = false
            hideLoading()
            binding.placeHolder.visibility = View.GONE
        }
        viewModel.errorResponseEvery.observe(viewLifecycleOwner) {
            isLoading = false
            hideLoading()
            binding.placeHolder.visibility = View.GONE
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
}