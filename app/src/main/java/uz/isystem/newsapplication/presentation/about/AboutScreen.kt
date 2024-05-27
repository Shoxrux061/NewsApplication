package uz.isystem.newsapplication.presentation.about

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.ScreenAboutBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment

class AboutScreen : BaseFragment(R.layout.screen_about){
    private val binding by viewBinding(ScreenAboutBinding::bind)
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

    }
}
