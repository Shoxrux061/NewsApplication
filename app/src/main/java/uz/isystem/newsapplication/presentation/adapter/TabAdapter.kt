package uz.isystem.newsapplication.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.isystem.newsapplication.presentation.main.home.category.business.BusinessFragment
import uz.isystem.newsapplication.presentation.main.home.category.entertainment.EntertainmentFragment
import uz.isystem.newsapplication.presentation.main.home.category.general.GeneralFragment
import uz.isystem.newsapplication.presentation.main.home.category.health.HealthFragment
import uz.isystem.newsapplication.presentation.main.home.category.since.SinceFragment
import uz.isystem.newsapplication.presentation.main.home.category.sports.SportsFragment
import uz.isystem.newsapplication.presentation.main.home.category.technology.TechnologyFragment

class TabAdapter(fragmentManager: FragmentManager, ls : Lifecycle) : FragmentStateAdapter(fragmentManager,ls) {
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> BusinessFragment()
        1 -> EntertainmentFragment()
        2 -> GeneralFragment()
        3 -> HealthFragment()
        4 -> SinceFragment()
        5 -> SportsFragment()
        else -> TechnologyFragment()
    }
}