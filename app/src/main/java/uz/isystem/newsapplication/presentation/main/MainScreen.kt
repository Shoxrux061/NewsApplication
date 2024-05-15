package uz.isystem.newsapplication.presentation.main

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.ScreenMainBinding
import uz.isystem.newsapplication.presentation.adapter.MainAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment

class MainScreen : BaseFragment(R.layout.screen_main){
    private val binding by viewBinding(ScreenMainBinding::bind)
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

        setPager()
    }
    private fun setPager() {
        val alphaAnimate = AnimationUtils.loadAnimation(context,R.anim.alpha)
        val alphaAnimateTwo = AnimationUtils.loadAnimation(context,R.anim.alpha2)
        val adapter = MainAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.popularID -> {
                    binding.viewPager.setCurrentItem(0, true)


                }
                R.id.categoryID -> {
                    binding.viewPager.setCurrentItem(1, true)
                }
                else -> {
                    binding.viewPager.setCurrentItem(2, true)
                }
            }
            return@setOnItemSelectedListener true
        }
    }
}