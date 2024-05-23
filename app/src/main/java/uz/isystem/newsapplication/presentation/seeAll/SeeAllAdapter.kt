package uz.isystem.newsapplication.presentation.seeAll

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.model.everything.Article
import uz.isystem.newsapplication.databinding.ItemCategoryBinding

class SeeAllAdapter : RecyclerView.Adapter<SeeAllAdapter.ViewHolder>() {

    lateinit var onClickItem: (Article) -> Unit
    lateinit var onPaginate: () -> Unit

    private val data = ArrayList<Article>()

    fun setData(data: List<Article>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData(){
        this.data.clear()
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: Article) {
            binding.image.load(data.urlToImage) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }
            binding.title.text = data.title
            binding.time.text = data.publishedAt
            binding.author.text = data.author ?: "unknown"
            binding.root.setOnClickListener {
                onClickItem.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var page = 0
        if (position >= data.size -5) {
            onPaginate.invoke()
            page++
            Log.d("TagPaginate", "onBindViewHolder: $page")
        }
        holder.bindData(data[position])
    }
}