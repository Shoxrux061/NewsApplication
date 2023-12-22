package uz.isystem.newsapplication.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.isystem.newsapplication.presentation.main.category.business.BusinessFragment
import uz.isystem.newsapplication.presentation.main.category.entertainment.EntertainmentFragment
import uz.isystem.newsapplication.presentation.main.category.general.GeneralFragment
import uz.isystem.newsapplication.presentation.main.category.health.HealthFragment
import uz.isystem.newsapplication.presentation.main.category.science.ScinceFragment
import uz.isystem.newsapplication.presentation.main.category.sports.SportsFragment
import uz.isystem.newsapplication.presentation.main.category.technology.TechnologyFragment

class TabAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0->EntertainmentFragment()
            1->BusinessFragment()
            2->GeneralFragment()
            3->ScinceFragment()
            4->SportsFragment()
            5->TechnologyFragment()
            else->HealthFragment()
        }
    }
}