package uz.isystem.newsapplication.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.PageHomeBinding
import uz.isystem.newsapplication.presentation.adapter.HomeAdapter
import uz.isystem.newsapplication.presentation.adapter.TabAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.MainScreenDirections

class HomePage : BaseFragment(R.layout.page_home) {
    private val binding by viewBinding(PageHomeBinding::bind)
    private val homeAdapter = HomeAdapter()
    private val viewModel: HomeViewModel by viewModels()
    private var isFirst = true
    private var category = "business"
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        val lang = getString(R.string.language)
        if (isFirst) {
            viewModel.getEverything(lang)
        }
        isFirst = false
        setAdapter()
        setTabLayout()
        observe()
        setActions()
    }

    private fun setTabLayout() {

    }

    private fun setAdapter() {
        val adapter = TabAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.recyclerView.adapter = homeAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.business)
                    tab.contentDescription = "business"
                }

                1 -> {
                    tab.text = getString(R.string.entertainment)
                    tab.contentDescription = "entertainment"

                }

                2 -> {
                    tab.text = getString(R.string.general)
                    tab.contentDescription = "general"

                }

                3 -> {
                    tab.text = getString(R.string.heath)
                    tab.contentDescription = "health"

                }

                4 -> {
                    tab.text = getString(R.string.scinece)
                    tab.contentDescription = "science"

                }

                5 -> {
                    tab.text = getString(R.string.sports)
                    tab.contentDescription = "sports"
                }

                6 -> {
                    tab.text = getString(R.string.technology)
                    tab.contentDescription = "technology"
                }

            }
        }.attach()
    }

    private fun setActions() {

        binding.searchBtn.setOnClickListener {
            nextScreen(MainScreenDirections.actionMainScreenToSearchPage())
        }

        binding.seeAllLatest.setOnClickListener {
            nextScreen(MainScreenDirections.actionMainScreenToSeeAllScreen(category))
        }

        binding.seeAllTrending.setOnClickListener {
            nextScreen(MainScreenDirections.actionMainScreenToSeeAllScreen("top"))
        }

        homeAdapter.onClickItem = {
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

        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager.currentItem = tab!!.position
                category = tab.contentDescription.toString()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }

    private fun observe() {
        viewModel.successResponseEvery.observe(viewLifecycleOwner) {
            setShimmer()
            homeAdapter.setData(it!!.articles)
        }
        viewModel.errorResponseEvery.observe(viewLifecycleOwner) {

        }

    }

    private fun setShimmer() {
        binding.parent.visibility = View.VISIBLE
        binding.shimmer.visibility = View.GONE
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