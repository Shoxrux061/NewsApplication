package uz.isystem.newsapplication.presentation.profile

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.ScreenProfileBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment

class ProfileScreen : BaseFragment(R.layout.screen_profile){
    private val binding by viewBinding(ScreenProfileBinding::bind)
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

    }
}

