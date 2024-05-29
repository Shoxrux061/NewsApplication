package uz.isystem.newsapplication.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import uz.isystem.newsapplication.data.model.user.UserDetailsModel
import uz.isystem.newsapplication.databinding.ScreenProfileBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment

class ProfileScreen : BaseFragment(R.layout.screen_profile) {
    private val binding by viewBinding(ScreenProfileBinding::bind)
    private lateinit var dbr: DatabaseReference
    private lateinit var vel: ValueEventListener
    private var isLoading = false
    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        dbr = FirebaseDatabase.getInstance().getReference(getString(R.string.path_users))
        getDataInDB()
        actionsListener()
    }

    private fun getDataInDB() {

        binding.emailEdt.setText(LocaleStorage.getObject().getEmail())

        vel = dbr.child(getString(R.string.path_details))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(UserDetailsModel::class.java).let {
                        binding.usernmaeEdt.setText(it?.userName ?: "")
                        binding.fullNameEdt.setText(it?.fullName ?: "")
                        binding.phoneNumberEdt.setText(it?.phoneNumber ?: "")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
                    Log.d("TagError", "onCancelled: $error")
                }
            })
    }

    private fun actionsListener() {
        binding.editBtn.setOnClickListener {
            setEditable(true)
            binding.confirmBtn.visibility = View.VISIBLE
            it.visibility = View.GONE
        }
        binding.confirmBtn.setOnClickListener {

        }

        binding.cacnel.setOnClickListener {
            setEditable(false)
            it.visibility = View.INVISIBLE
        }
    }

    private fun checkIsEdited() {

        Toast.makeText(context, "IsClick", Toast.LENGTH_SHORT).show()

        if (binding.fullNameEdt.text.isBlank()) {
            binding.fullNameEdt.error = getString(R.string.inputError)
            return
        } else if (binding.phoneNumberEdt.text.isBlank()) {
            binding.phoneNumberEdt.error = getString(R.string.inputError)
            return
        } else if (binding.usernmaeEdt.text.isBlank()) {
            binding.usernmaeEdt.error = getString(R.string.inputError)
            return
        }

        val username = binding.usernmaeEdt.text.toString()
        val fullName = binding.fullNameEdt.text.toString()
        val phoneNumber = binding.fullNameEdt.text.toString()
        val email = LocaleStorage.getObject().getEmail()

        val data = UserDetailsModel(
            userName = username,
            fullName = fullName,
            phoneNumber = phoneNumber,
            email = email
        )
        dbr.child(getString(R.string.path_details)).setValue(data).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, getString(R.string.success_is_edited), Toast.LENGTH_SHORT)
                    .show()
                findNavController().popBackStack()
            } else{
                Toast.makeText(
                    context, it.exception.toString(), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setEditable(isEditable: Boolean) {
        binding.fullNameEdt.isEnabled = isEditable
        binding.usernmaeEdt.isEnabled = isEditable
        binding.phoneNumberEdt.isEnabled = isEditable
    }
}

