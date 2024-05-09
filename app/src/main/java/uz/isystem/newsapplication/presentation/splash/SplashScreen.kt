package uz.isystem.newsapplication.presentation.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.model.ViewPagerModel
import uz.isystem.newsapplication.databinding.ScreenSplashBinding
import uz.isystem.newsapplication.presentation.adapter.ViewPagerAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment

@SuppressLint("CustomSplashScreen")
class SplashScreen : BaseFragment(R.layout.screen_splash) {
    private val binding by viewBinding(ScreenSplashBinding::bind)
    private val adapter by lazy { ViewPagerAdapter() }
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

        setPager()
        addPagerData()
    }

    private fun addPagerData() {

        binding.dotsIndicator.attachTo(binding.viewPager)


        val data = listOf(
            ViewPagerModel(
                image = R.drawable.splash1_image,
                title1 = getString(R.string.splash1Title1),
                title2 = getString(R.string.splash1title2)
            ),
            ViewPagerModel(
                image = R.drawable.splash2_image,
                title1 = getString(R.string.splash2title1),
                title2 = getString(R.string.splash2title2)
            ),
            ViewPagerModel(
                image = R.drawable.splash3_iamge,
                title1 = getString(R.string.splash3title1),
                title2 = getString(R.string.splash3title2)
            )
        )
        adapter.setData(data)
    }

    private fun setPager() {
        binding.viewPager.adapter = adapter

        binding.viewPager.isUserInputEnabled = false

        binding.nextBtn.setOnClickListener {
            if (binding.viewPager.currentItem < 3) {
                binding.viewPager.currentItem += 1
            }
            check()
        }
        binding.backBtn.setOnClickListener {
            if (binding.viewPager.currentItem > 0) {
                binding.viewPager.currentItem -= 1
            }
            check()
        }
    }

    private fun check() {

        val text = getString(R.string.get_started)

        if (binding.viewPager.currentItem == 2 && binding.nextBtn.text == text) {
            findNavController().navigate(SplashScreenDirections.actionSplashScreenToRegistrationScreen())
        }

        if (binding.viewPager.currentItem == 2) {
            binding.nextBtn.text = text
        } else {
            binding.nextBtn.text = getString(R.string.next_btn)
        }
    }
}