package uz.isystem.newsapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.model.everything.Article
import uz.isystem.newsapplication.databinding.HomeItemBinding
import uz.isystem.newsapplication.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    lateinit var onClickItem : (Article) -> Unit

    private val data = ArrayList<Article>()

    fun setData(data:List<Article>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindData(data : Article){
            binding.image.load(data.urlToImage){
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }
            binding.title.text = data.title
            binding.time.text = data.publishedAt
            binding.author.text = data.author?:"unknown"
            binding.root.setOnClickListener {
                //onClickItem.invoke(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }
}