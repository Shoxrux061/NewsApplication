package uz.isystem.newsapplication.presentation.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.cache.LocaleStorage
import uz.isystem.newsapplication.data.model.user.UserDetailsModel
import uz.isystem.newsapplication.databinding.ScreenProfileBinding
import uz.isystem.newsapplication.presentation.base.BaseFragment
import java.io.File

class ProfileScreen : BaseFragment(R.layout.screen_profile) {
    private val binding by viewBinding(ScreenProfileBinding::bind)
    private lateinit var dbr: DatabaseReference
    private lateinit var vel: ValueEventListener
    private var isLoading = false
    private var isViewDestroyed = false
    private lateinit var fbs: StorageReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        fbs = FirebaseStorage.getInstance().getReference(auth.currentUser?.uid ?: "")
            .child(auth.currentUser!!.uid)
        dbr = FirebaseDatabase.getInstance().getReference(getString(R.string.path_users))
            .child(auth.currentUser?.uid ?: "")
            .child(getString(R.string.path_details))
        if (!isLoading) {
            getDataInDB()
        }
        actionsListener()

    }

    private fun getDataInDB() {
        setLoading(true)

        val localFile = File.createTempFile("images", "jpg")

        fbs.getFile(localFile).addOnSuccessListener {
            if (isViewDestroyed) return@addOnSuccessListener
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.iamge.setImageBitmap(bitmap)

        }.addOnFailureListener {
            binding.iamge.setImageResource(R.drawable.person_placeholder)
        }
        vel = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isViewDestroyed) return

                val userDetails = snapshot.getValue(UserDetailsModel::class.java)
                binding.usernmaeEdt.setText(userDetails?.userName ?: "")
                binding.fullNameEdt.setText(userDetails?.fullName ?: "")
                binding.phoneNumberEdt.setText(userDetails?.phoneNumber ?: "")
                binding.emailEdt.setText(LocaleStorage.getObject().getEmail())
                setLoading(false)
            }

            override fun onCancelled(error: DatabaseError) {
                if (isViewDestroyed) return

                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
                Log.d("TagError", "onCancelled: $error")
                setLoading(false)
            }
        }

        dbr.addValueEventListener(vel)
    }

    private fun actionsListener() {
        binding.editBtn.setOnClickListener {
            setEditable(true)
        }
        binding.confirmBtn.setOnClickListener {
            if (!isLoading) {
                checkIsEdited()
            }
        }

        binding.cacnel.setOnClickListener {
            setEditable(false)
        }

        binding.addImage.setOnClickListener {
            pickImage()
        }
    }

    private fun pickImage() {
        val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 101) {
                if (isViewDestroyed) return

                val uri = data?.data
                binding.iamge.setImageURI(uri)
                setLoading(true)
                fbs.putFile(uri!!).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        setLoading(false)
                    } else {
                        setLoading(false)
                    }
                }
            }
        }
    }

    private fun checkIsEdited() {
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
        val phoneNumber = binding.phoneNumberEdt.text.toString()
        val email = LocaleStorage.getObject().getEmail()

        val data = UserDetailsModel(
            userName = username,
            fullName = fullName,
            phoneNumber = phoneNumber,
            email = email
        )

        setLoading(true)
        dbr.setValue(data).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context, getString(R.string.success_is_edited), Toast.LENGTH_SHORT)
                    .show()
                setLoading(false)
            } else {
                setLoading(false)
                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
                Log.d("TAGError", "checkIsEdited: ${it.exception}")
            }
        }
    }

    private fun setEditable(isEditable: Boolean) {
        binding.fullNameEdt.isEnabled = isEditable
        binding.usernmaeEdt.isEnabled = isEditable
        binding.phoneNumberEdt.isEnabled = isEditable
        if (isEditable) {
            if (isViewDestroyed) return
            binding.editBtn.visibility = View.GONE
            binding.confirmBtn.visibility = View.VISIBLE
            binding.cacnel.visibility = View.VISIBLE
        } else {
            if (isViewDestroyed) return
            binding.editBtn.visibility = View.VISIBLE
            binding.confirmBtn.visibility = View.GONE
            binding.cacnel.visibility = View.INVISIBLE
        }
    }

    private fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        if (isLoading) {
            if (isViewDestroyed) return
            binding.progressBar.visibility = View.VISIBLE
            binding.parent.alpha = 0.5f
            setEditable(false)
            binding.editBtn.isClickable = false
        } else {
            if (isViewDestroyed) return
            binding.progressBar.visibility = View.GONE
            binding.parent.alpha = 1f
            binding.editBtn.isClickable = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewDestroyed = true
        if (::dbr.isInitialized && ::vel.isInitialized) {
            Log.d("ProfileScreen", "Removing event listener")
            dbr.removeEventListener(vel)
        } else {
            Log.d("ProfileScreen", "dbr or vel is not initialized")
        }
    }
}