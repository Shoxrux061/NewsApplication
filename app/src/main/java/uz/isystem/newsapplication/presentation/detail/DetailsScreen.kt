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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
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
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    private lateinit var dbr: DatabaseReference
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

        setFirebase()
        setData()
        setActions()
    }

    private fun setFirebase() {
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid ?: ""
        dbr = FirebaseDatabase.getInstance().getReference(getString(R.string.path_users)).child(uid)
            .child(getString(R.string.path_saved))
    }

    private fun setActions() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.saveBtn.setOnClickListener {

            savePost()
        }
        binding.shareBtn.setOnClickListener {

            showShareDialog()
        }
        binding.link.setOnClickListener {

            showLinkDialog()
        }
    }

    private fun showLinkDialog() {
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

    private fun showShareDialog() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, args.url)

        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this link!")

        startActivity(Intent.createChooser(intent, "Share link via"))
    }

    private fun savePost() {

        if (!isSaved) {
            isSaved = true
            binding.saveBtn.setImageResource(R.drawable.ic_saved)
            val data = RoomArticles(
                image = args.imageUrl,
                url = args.url,
                title = args.title,
                description = args.description,
                publishedAt = args.publishedAt,
                author = args.author,
                content = args.content
            )
            room?.addNews(data)
            dbr.setValue(room?.getAllSaved())
        } else {
            isSaved = false
            binding.saveBtn.setImageResource(R.drawable.ic_not_saved)
            room?.deleteByUrl(args.url)
            dbr.setValue(room?.getAllSaved())
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
        binding.publishedAt.text = formatDate(args.publishedAt)
        binding.image.load(args.imageUrl) {
            placeholder(R.drawable.placeholder)
            error(R.drawable.placeholder)
        }
        if (args.content !="null"){
            binding.content.text = args.content

        }else{
            binding.content.text = "..."
        }
        if (args.title!="null"){
            binding.title.text = args.title
        }else{
            binding.title.text = "..."
        }
        if(args.description !="null") {
            binding.description.text = args.description
        }else{
            binding.description.text = "..."
        }
        val author = if(args.author != "null") {
             "${getString(R.string.by)}: ${args.author}"
        } else {
            "unknown"
        }
        binding.author.text = author
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
}