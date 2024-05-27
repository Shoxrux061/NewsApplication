package uz.isystem.newsapplication.presentation.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import uz.isystem.newsapplication.databinding.ScreenLoginBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment

class LoginScreen : BaseFragment(R.layout.screen_login) {

    private val binding by viewBinding(ScreenLoginBinding::bind)
    private lateinit var auth: FirebaseAuth
    private var isLoading = false
    private var errorText: String? = null
    private var errorPassword: String? = null
    private var errorName: String? = null
    override fun onCreate(view: View, savedInstanceState: Bundle?) {

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
            nextScreen(LoginScreenDirections.actionLoginScreenToRegistrationScreen())
        }
    }

    private fun nextScreen(navDirections: NavDirections) {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in)
            .setExitAnim(R.anim.slide_out)
            .setPopEnterAnim(R.anim.slide_in_reverse)
            .setPopExitAnim(R.anim.slide_out_reverse)
            .build()
        findNavController().navigate(navDirections, navOptions)
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

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { result ->

            if (result.isSuccessful) {
                isLoading = false
                setLoading()
                LocaleStorage.getObject().setIsSigned(true)
                LocaleStorage.getObject().setEmailNPassword(email, password)
                nextScreen(LoginScreenDirections.actionLoginScreenToMainScreen())
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


}
