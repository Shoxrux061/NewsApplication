package uz.isystem.newsapplication.presentation.main.category

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.PageCategoryBinding
import uz.isystem.newsapplication.presentation.NewsViewModel
import uz.isystem.newsapplication.presentation.adapter.HeaderAdapter
import uz.isystem.newsapplication.presentation.adapter.TabAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.MainScreenDirections

class CategoryPage : BaseFragment(R.layout.page_category) {
    private val binding by viewBinding(PageCategoryBinding::bind)
    private val viewModel: NewsViewModel by viewModels()
    private val headerAdapter = HeaderAdapter()
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        viewModel.getEverything(q = "o", getString(R.string.language))
        observe()
        setHeaderAdapter()
        setTabs()
        setTabChanges()
        setActions()
    }
    private fun setActions() {
        headerAdapter.onClickItem = {
            findNavController().navigate(
                MainScreenDirections.actionMainScreenToDetailsScreen(
                    title = it.title?:"no info",
                    description = it.description?:"no info",
                    imageUrl = it.urlToImage?:"",
                    url = it.url?:"",
                    publishedAt = it.publishedAt,
                    author = it.author?:"unknown"
                )
            )
        }
    }

    private fun observe() {
        viewModel.successResponseEvery.observe(viewLifecycleOwner) {
            headerAdapter.setData(it!!.articles)
        }
        viewModel.errorResponseEvery.observe(viewLifecycleOwner) {
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setHeaderAdapter() {
        binding.topViewPager.adapter = headerAdapter
    }

    private fun setTabChanges() {
        binding.viewPager.isUserInputEnabled = false
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun setTabs() {
        val adapter = TabAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.entertainment)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.business)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.general)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.scinece)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.sports)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.technology)))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(getString(R.string.heath)))
    }
}