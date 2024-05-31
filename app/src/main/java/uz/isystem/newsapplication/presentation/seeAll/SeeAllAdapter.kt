package uz.isystem.newsapplication.presentation.seeAll

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.model.everything.Article
import uz.isystem.newsapplication.databinding.ItemCategoryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class SeeAllAdapter(private val context:Context) : RecyclerView.Adapter<SeeAllAdapter.ViewHolder>() {

    lateinit var onClickItem: (Article) -> Unit
    lateinit var onPaginate: () -> Unit

    private val data = ArrayList<Article>()

    fun setData(data: List<Article>) {
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date = inputFormat.parse(inputDate)

        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale(context.getString(R.string.language)))
        val dayOfMonthFormat = SimpleDateFormat("d", Locale(context.getString(R.string.language)))
        val monthFormat = SimpleDateFormat("MMMM", Locale(context.getString(R.string.language)))
        val yearFormat = SimpleDateFormat("yyyy", Locale(context.getString(R.string.language)))

        val dayOfWeek = dayOfWeekFormat.format(date!!)
        val dayOfMonth = dayOfMonthFormat.format(date)
        val month = monthFormat.format(date)
        val year = yearFormat.format(date)

        return "$dayOfWeek $dayOfMonth $month $year"
    }


    inner class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(data: Article) {
            binding.image.load(data.urlToImage) {
                placeholder(R.drawable.placeholder)
                error(R.drawable.placeholder)
            }
            val date = formatDate(data.publishedAt)
            binding.title.text = data.title
            binding.time.text = date
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