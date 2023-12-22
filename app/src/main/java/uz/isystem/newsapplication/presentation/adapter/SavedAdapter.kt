package uz.isystem.newsapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.room.RoomArticles
import uz.isystem.newsapplication.databinding.ItemCategoryBinding

class SavedAdapter : RecyclerView.Adapter<SavedAdapter.ViewHolder>() {

    lateinit var onClickItem : (RoomArticles) -> Unit

    private val data = ArrayList<RoomArticles>()

    fun setData(data:List<RoomArticles>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindData(data : RoomArticles){
            binding.image.load(data.image){
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }
            binding.author.text = data.author
            binding.author.isFocusable = true
            binding.title.text = data.title
            binding.root.setOnClickListener {
                onClickItem.invoke(data)
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