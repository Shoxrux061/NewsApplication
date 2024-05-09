package uz.isystem.newsapplication.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.isystem.newsapplication.data.model.ViewPagerModel
import uz.isystem.newsapplication.databinding.ItemSplashBinding

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    private val data = ArrayList<ViewPagerModel>()

    fun setData(data: List<ViewPagerModel>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemSplashBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data:ViewPagerModel) {
            binding.image.setImageResource(data.image)
            binding.title1.text = data.title1
            binding.title2.text = data.title2
            binding.image.setImageResource(data.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSplashBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }
}