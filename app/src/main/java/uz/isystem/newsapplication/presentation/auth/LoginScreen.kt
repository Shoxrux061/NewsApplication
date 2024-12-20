package uz.isystem.newsapplication.presentation.auth

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import uz.isystem.newsapplication.databinding.ScreenLoginBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.extations.changeScreen

class LoginScreen : BaseFragment(R.layout.screen_login) {

    private val binding by viewBinding(ScreenLoginBinding::bind)
    private var auth: FirebaseAuth? = null
    private var isLoading = false
    private var errorText: String? = null
    private var errorPassword: String? = null
    private var errorName: String? = null
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        onBackPressed()
        auth = FirebaseAuth.getInstance()
        errorName = getString(R.string.nameError)
        errorPassword = getString(R.string.errorPassword)
        errorText = getString(R.string.inputError)
        actionsListener()


    }

    private fun actionsListener() {
        binding.btnCreateAcc.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            login(email = email, password = password)
        }
        binding.goToReg.setOnClickListener {
            findNavController().changeScreen(LoginScreenDirections.actionLoginScreenToRegistrationScreen())
        }
    }


    private fun login(email: String, password: String) {

        if (email.isBlank()) {
            binding.edtEmail.error = errorText
            return
        }
        if (password.isBlank() || password.length < 8) {
            binding.edtPassword.error = errorPassword
            return
        }

        isLoading = true
        setLoading()

        auth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener { result ->

            if (result.isSuccessful) {
                isLoading = false
                setLoading()
                LocaleStorage.getObject().setIsSigned(true)
                LocaleStorage.getObject().setEmailNPassword(email, password)
                findNavController().changeScreen(LoginScreenDirections.actionLoginScreenToMainScreen())
            } else {
                isLoading = false
                setLoading()
                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
                Log.d("TAGError", "login:${result.exception?.message}")
            }

        }

    }

    private fun setLoading() {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.edtPassword.isEnabled = false
            binding.edtEmail.isEnabled = false
            binding.btnCreateAcc.isClickable = false
            binding.btnFacebook.isClickable = false
            binding.btnGoogle.isClickable = false
            binding.parent.alpha = 0.5f
        } else {
            binding.progressBar.visibility = View.GONE
            binding.edtPassword.isEnabled = true
            binding.edtEmail.isEnabled = true
            binding.btnCreateAcc.isClickable = true
            binding.btnFacebook.isClickable = true
            binding.btnGoogle.isClickable = true
            binding.parent.alpha = 1f
        }
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (doubleBackToExitPressedOnce) {
                        requireActivity().finish()
                        return
                    }
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.double_tap),
                        Toast.LENGTH_SHORT
                    ).show()

                    Handler().postDelayed({
                        doubleBackToExitPressedOnce = false
                    }, 2000)
                }
            })
    }
}
