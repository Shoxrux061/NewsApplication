package uz.isystem.newsapplication.presentation.adapter

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import uz.isystem.newsapplication.data.model.everything.Article
import uz.isystem.newsapplication.databinding.HomeItemBinding
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    lateinit var onClickItem: (Article) -> Unit

    private val data = ArrayList<Article>()

    fun setData(data: List<Article>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: HomeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: Article) {
            binding.image.load(data.urlToImage) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }
            binding.title.text = data.title
            binding.root.setOnClickListener {
                onClickItem.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }
}