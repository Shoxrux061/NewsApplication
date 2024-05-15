package uz.isystem.newsapplication.presentation.main.category

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.PageCategoryBinding
import uz.isystem.newsapplication.presentation.NewsViewModel
import uz.isystem.newsapplication.presentation.adapter.HeaderAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.MainScreenDirections

class CategoryPage : BaseFragment(R.layout.page_category) {
    private val binding by viewBinding(PageCategoryBinding::bind)
    private val viewModel: NewsViewModel by viewModels()

    override fun onCreate(view: View, savedInstanceState: Bundle?) {
    }


}