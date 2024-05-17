package uz.isystem.newsapplication.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.isystem.newsapplication.data.model.everything.Article
import uz.isystem.newsapplication.data.model.everything.EverythingResponse
import uz.isystem.newsapplication.databinding.FragmentCategoryBinding
import kotlin.math.log

class ParentCategoryAdapter : RecyclerView.Adapter<ParentCategoryAdapter.ViewHolder>() {

    private val data = ArrayList<EverythingResponse>()

    fun clearData() {
        this.data.clear()
        notifyDataSetChanged()
    }

    fun setData(data: EverythingResponse) {
        this.data.add(data)
        notifyDataSetChanged()
    }


    inner class ViewHolder(private val binding: FragmentCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val adapter = ChildCategoryAdapter()

        init {
            binding.recyclerView.adapter = adapter
        }

        fun bindData(data: EverythingResponse) {
            Log.d("TAGAdapter", "bindData: $data")
            adapter.setData(data.articles)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }
}