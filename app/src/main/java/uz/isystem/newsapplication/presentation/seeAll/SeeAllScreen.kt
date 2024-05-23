package uz.isystem.newsapplication.presentation.seeAll

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.ScreenSeeAllBinding
import uz.isystem.newsapplication.presentation.adapter.CategoryAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment

class SeeAllScreen : BaseFragment(R.layout.screen_see_all) {
    private val binding by viewBinding(ScreenSeeAllBinding::bind)
    private val viewModel: SeeAllViewModel by viewModels()
    private val adapter by lazy { CategoryAdapter() }
    private val args: SeeAllScreenArgs by navArgs()
    private lateinit var lang: String
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        lang = getString(R.string.language)
        setAdapter()
        sendRequest()
        observe()

    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }

    private fun observe() {
        viewModel.successResponseCategory.observe(viewLifecycleOwner){
            adapter.setData(it!!.articles)
        }
        viewModel.successResponseEvery.observe(viewLifecycleOwner){
            adapter.setData(it!!.articles)
        }
    }

    private fun sendRequest() {
        if (args.category == "top") {
            viewModel.getEverything(lang)
        } else {
            viewModel.getCategories(lang, args.category)
        }
    }
}