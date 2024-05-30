package uz.isystem.newsapplication.presentation.auth

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import uz.isystem.newsapplication.data.model.user.UserDetailsModel
import uz.isystem.newsapplication.data.model.user.UserModel
import uz.isystem.newsapplication.databinding.ScreenRegistrationBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment

class RegistrationScreen : BaseFragment(R.layout.screen_registration) {

    private val binding by viewBinding(ScreenRegistrationBinding::bind)
    private lateinit var auth: FirebaseAuth
    private lateinit var dbr: DatabaseReference
    private var errorText: String? = null
    private var errorPassword: String? = null
    private var errorName: String? = null
    private var isLoading = false
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        onBackPressed()
        auth = FirebaseAuth.getInstance()
        dbr = FirebaseDatabase.getInstance().getReference(getString(R.string.path_users))
        errorName = getString(R.string.nameError)
        errorPassword = getString(R.string.errorPassword)
        errorText = getString(R.string.inputError)
        actionsListener()

    }

    private fun actionsListener() {
        binding.btnCreateAcc.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val name = binding.edtUsername.text.toString().trim()
            createAcc(email = email, password = password, name = name)
        }
        binding.goToLogin.setOnClickListener {
            nextScreen(RegistrationScreenDirections.actionRegistrationScreenToLoginScreen())
        }
    }

    private fun createAcc(email: String, password: String, name: String) {

        if (email.isBlank()) {
            binding.edtEmail.error = errorText
            return
        }
        if (password.isBlank()) {
            binding.edtPassword.error = errorText
            return
        }
        if (binding.edtPassword.text.length < 8) {
            binding.edtPassword.error = errorPassword
            return
        }
        if (binding.edtUsername.text.isBlank()) {
            binding.edtUsername.error = errorText
        }
        if (binding.edtUsername.text.length < 3) {
            binding.edtUsername.error = errorName
        }
        isLoading = true
        setLoading()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                isLoading = false
                setLoading()
                val uid = auth.uid.toString()
                val user = UserModel(
                    password = password,
                    email = email,
                    name = name
                )
                dbr.child(uid).child(getString(R.string.path_details)).setValue(user)
                val userDetails = UserDetailsModel(
                    userName = name
                )
                dbr.child(uid).child(getString(R.string.path_details)).setValue(userDetails)
                LocaleStorage.getObject().setIsSigned(true)
                LocaleStorage.getObject().setEmailNPassword(email, password)
                nextScreen(RegistrationScreenDirections.actionRegistrationScreenToMainScreen())

            } else {
                isLoading = false
                setLoading()
                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
            }
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