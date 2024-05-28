package uz.isystem.newsapplication.presentation.detail

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.room.RoomArticles
import uz.isystem.newsapplication.data.room.RoomDataBase
import uz.isystem.newsapplication.databinding.ScreenDetailsBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment
import java.text.SimpleDateFormat
import java.util.Locale

class DetailsScreen : BaseFragment(R.layout.screen_details) {
    private val binding by viewBinding(ScreenDetailsBinding::bind)
    private val args: DetailsScreenArgs by navArgs()
    private var isSaved = false
    private val room = RoomDataBase.getInstance()
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        setData()
        setActions()
    }

    private fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date = inputFormat.parse(inputDate)

        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale(getString(R.string.language)))
        val dayOfMonthFormat = SimpleDateFormat("d", Locale(getString(R.string.language)))
        val monthFormat = SimpleDateFormat("MMMM", Locale(getString(R.string.language)))
        val yearFormat = SimpleDateFormat("yyyy", Locale(getString(R.string.language)))

        val dayOfWeek = dayOfWeekFormat.format(date!!)
        val dayOfMonth = dayOfMonthFormat.format(date)
        val month = monthFormat.format(date)
        val year = yearFormat.format(date)

        return "$dayOfWeek $dayOfMonth $month $year"
    }

    private fun setActions() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.saveBtn.setOnClickListener {
            if (!isSaved) {
                isSaved = true
                binding.saveBtn.setImageResource(R.drawable.ic_saved)
                val data = RoomArticles(
                    image = args.imageUrl,
                    url = args.url,
                    title = args.title,
                    description = args.description,
                    publishedAt = args.publishedAt,
                    author = args.author
                )
                room?.addNews(data)
            } else {
                isSaved = false
                binding.saveBtn.setImageResource(R.drawable.ic_not_saved)
                room?.deleteByUrl(args.url)
            }
        }
        binding.shareBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, args.url)

            intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this link!")

            startActivity(Intent.createChooser(intent, "Share link via"))
        }
        binding.link.setOnClickListener{
            val alertDialogBuilder = AlertDialog.Builder(requireContext())

            alertDialogBuilder.setTitle(getString(R.string.app_name))
            alertDialogBuilder.setMessage(getString(R.string.wantGoLink))

            alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(args.url))
                startActivity(intent)
                dialog.dismiss()
            }

            alertDialogBuilder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun setData() {
        if (room!!.getAllUrl().contains(args.url)) {
            isSaved = true
            binding.saveBtn.setImageResource(R.drawable.ic_saved)
        } else {
            isSaved = false
            binding.saveBtn.setImageResource(R.drawable.ic_not_saved)
        }

        binding.author.isSelected = true
        binding.content.text = args.content
        binding.publishedAt.text = formatDate(args.publishedAt)
        binding.image.load(args.imageUrl){
            placeholder(R.drawable.placeholder)
            error(R.drawable.placeholder)
        }
        binding.title.text = args.title
        binding.description.text = args.description
        val author = "${getString(R.string.by)}: ${args.author}"
        binding.author.text = author
    }

}

