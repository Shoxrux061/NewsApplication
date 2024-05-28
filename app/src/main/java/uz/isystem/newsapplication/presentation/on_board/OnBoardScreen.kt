package uz.isystem.newsapplication.presentation.on_board

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import uz.isystem.newsapplication.data.model.ViewPagerModel
import uz.isystem.newsapplication.databinding.ScreenOnBoardBinding
import uz.isystem.newsapplication.presentation.adapter.ViewPagerAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment

class OnBoardScreen : BaseFragment(R.layout.screen_on_board) {
    private val binding by viewBinding(ScreenOnBoardBinding::bind)
    private val adapter by lazy { ViewPagerAdapter() }
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

        handleOnBack()
        setPager()
        addPagerData()
    }

    private fun handleOnBack() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.viewPager.currentItem > 0) {
                    binding.viewPager.currentItem--
                } else {
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
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
            LocaleStorage.getObject().setIsNotFirst()
            nextScreen(OnBoardScreenDirections.actionOnBoardScreenToRegistrationScreen())
        }

        if (binding.viewPager.currentItem == 2) {
            binding.nextBtn.text = text
        } else {
            binding.nextBtn.text = getString(R.string.next_btn)
        }
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