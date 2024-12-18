package uz.isystem.newsapplication.presentation.about

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.databinding.ScreenAboutBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment

class AboutScreen : BaseFragment(R.layout.screen_about) {
    private val binding by viewBinding(ScreenAboutBinding::bind)
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), R.color.background_color)
        listenActions()

    }

    private fun listenActions() {


        binding.phone.setOnClickListener {
            showPhoneDialog()
        }

        binding.telegramm.setOnClickListener {
            showLinkDialog("https://t.me/ktl_06")
        }

        binding.instagramm.setOnClickListener {
            showLinkDialog("https://www.instagram.com/urmanov_06?igsh=bnZ3Y3dkaTJmOWJ3")
        }

        binding.gitHub.setOnClickListener {
            showLinkDialog("https://github.com/Shoxrux061")
        }

        binding.emaail.setOnClickListener {
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("email", "shoxruxurmanov2@gmail.com")
            clipboard.setPrimaryClip(clip)
        }
    }

    private fun showPhoneDialog() {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:+998903973088")
        }
        startActivity(intent)
    }

    private fun showLinkDialog(link: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        alertDialogBuilder.setTitle(getString(R.string.app_name))
        alertDialogBuilder.setMessage(getString(R.string.wantGoLink))

        alertDialogBuilder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
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
