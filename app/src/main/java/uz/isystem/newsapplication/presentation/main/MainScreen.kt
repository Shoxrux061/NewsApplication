package uz.isystem.newsapplication.presentation.main

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.ScreenMainBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment

class MainScreen : BaseFragment(R.layout.screen_main){
    private val binding by viewBinding(ScreenMainBinding::bind)
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

        setPager()
        onBackPressed()
    }
    private fun setPager() {
        val adapter = MainAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.popularID -> {
                    binding.viewPager.setCurrentItem(0, true)
                }
                R.id.savedID -> {
                    binding.viewPager.setCurrentItem(1, true)
                }
                else -> {
                    binding.viewPager.setCurrentItem(2, true)
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val currentItem = binding.viewPager.currentItem
                val navCurrentItem = binding.bottomNavigation.selectedItemId
                if (currentItem > 0) {
                    binding.viewPager.currentItem--
                    if(navCurrentItem == R.id.profileID){
                        binding.bottomNavigation.selectedItemId = R.id.savedID
                    }else if(navCurrentItem == R.id.savedID){
                        binding.bottomNavigation.selectedItemId = R.id.popularID
                    }
                } else {
                    if (doubleBackToExitPressedOnce) {
                        requireActivity().finish()
                        return
                    }

                    doubleBackToExitPressedOnce = true
                    Toast.makeText(requireContext(), getString(R.string.double_tap), Toast.LENGTH_SHORT).show()

                    Handler().postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                }
            }
        })
    }


}