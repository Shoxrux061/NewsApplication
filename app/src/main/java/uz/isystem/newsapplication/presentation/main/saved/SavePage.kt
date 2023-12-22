package uz.isystem.newsapplication.presentation.main.saved

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.model.everything.Article
import uz.isystem.newsapplication.data.room.RoomDataBase
import uz.isystem.newsapplication.databinding.PageSaveBinding
import uz.isystem.newsapplication.presentation.adapter.CategoryAdapter
import uz.isystem.newsapplication.presentation.adapter.SavedAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.main.MainScreenDirections

class SavePage : BaseFragment(R.layout.page_save){
    private val binding by viewBinding(PageSaveBinding::bind)
    private val adapter = SavedAdapter()
    private val room = RoomDataBase.getInstance()
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        setAdapter()
        setData()
        setActions()
    }

    private fun setActions() {
        adapter.onClickItem={
            findNavController().navigate(MainScreenDirections.actionMainScreenToDetailsScreen(
                description = it.description.toString(),
                title = it.title,
                imageUrl = it.image.toString(),
                url = it.url,
                publishedAt = it.publishedAt.toString(),
                author = it.author.toString()
            ))
        }
    }

    private fun setData() {
        if(room!!.getAllSaved().isEmpty()){
            binding.empty.visibility = View.VISIBLE
        }else {
            adapter.setData(room.getAllSaved())
        }
    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }
}