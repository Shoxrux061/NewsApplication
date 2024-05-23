package uz.isystem.newsapplication.presentation.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.isystem.newsapplication.presentation.main.home.HomePage
import uz.isystem.newsapplication.presentation.main.saved.SavePage
import uz.isystem.newsapplication.presentation.main.settings.SettingsPage

class MainAdapter(fragmentManager: FragmentManager, ls : Lifecycle) : FragmentStateAdapter(fragmentManager,ls) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> HomePage()
        1 -> SavePage()
        else -> SettingsPage()
    }
}