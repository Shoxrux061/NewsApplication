package uz.isystem.newsapplication.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.model.everything.EverythingResponse
import uz.isystem.newsapplication.databinding.PageHomeBinding
import uz.isystem.newsapplication.presentation.adapter.HomeAdapter
import uz.isystem.newsapplication.presentation.adapter.ParentCategoryAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment

class HomePage : BaseFragment(R.layout.page_home) {
    private val binding by viewBinding(PageHomeBinding::bind)
    private val multiAdapter = ParentCategoryAdapter()
    private val homeAdapter = HomeAdapter()
    private val viewModel: HomeViewModel by viewModels()
    private val multiData = ArrayList<EverythingResponse>()
    private var dataCount = 0
    private var isFirst = false
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        val lang = getString(R.string.language)
        if (!isFirst) {
            viewModel.getEverything(lang)
            viewModel.getCategories(lang,"business")
            viewModel.getCategories(lang,"sports")
        }
        isFirst = true
        setAdapter()
        setTabLayout()
        observe()
        setActions()
    }

    private fun setTabLayout() {

    }


    private fun setAdapter() {
        binding.recyclerView.adapter = homeAdapter
        binding.viewPager.adapter = multiAdapter
    }

    private fun setActions() {
        homeAdapter.onClickItem = {

        }
    }

    private fun observe() {
        viewModel.successResponseEvery.observe(viewLifecycleOwner) {
            homeAdapter.setData(it!!.articles)
        }
        viewModel.errorResponseEvery.observe(viewLifecycleOwner) {

        }

        viewModel.successResponseCategory.observe(viewLifecycleOwner) {
            multiData.add(it!!)
            multiAdapter.setData(multiData[dataCount])
            dataCount++
        }
    }
}

