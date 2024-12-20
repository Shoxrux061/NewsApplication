package uz.isystem.newsapplication.presentation.profile

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.checkSelfPermission
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
    private var dbr: DatabaseReference? = null
    private var vel: ValueEventListener? = null
    private var isLoading = false
    private var isViewDestroyed = false
    private var fbs: StorageReference? = null
    private var auth: FirebaseAuth? = null
    private var selectedUri: Uri? = null


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                launchImagePicker()
            } else {
                Toast.makeText(requireContext(), "No Permission", Toast.LENGTH_SHORT).show()
            }
        }
    private lateinit var getContent: ActivityResultLauncher<String>


    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        registerLauncher()
        auth = FirebaseAuth.getInstance()
        fbs = FirebaseStorage.getInstance().getReference(auth?.currentUser?.uid ?: "")
            .child(auth?.currentUser!!.uid)
        dbr = FirebaseDatabase.getInstance().getReference(getString(R.string.path_users))
            .child(auth?.currentUser?.uid ?: "")
            .child(getString(R.string.path_details))
        if (!isLoading) {
            getDataInDB()
        }
        actionsListener()

    }

    private fun registerLauncher() {
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.iamge.setImageURI(uri)
                selectedUri = uri
                setLoading(true)
                putImageOnFbs()
            }
        }
    }

    private fun putImageOnFbs() {
        fbs?.putFile(selectedUri!!)?.addOnCompleteListener { result ->
            if (result.isSuccessful) {
                setLoading(false)
            } else {
                setLoading(false)
            }
        }
    }

    private fun getDataInDB() {
        setLoading(true)

        val localFile = File.createTempFile("images", "jpg")

        fbs?.getFile(localFile)?.addOnSuccessListener {
            if (isViewDestroyed) return@addOnSuccessListener
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.iamge.setImageBitmap(bitmap)

        }?.addOnFailureListener {
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

        vel?.let {
            dbr?.addValueEventListener(it)
        }
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
            checkAndRequestPermission()
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
        dbr?.setValue(data)?.addOnCompleteListener {
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

    private fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                checkPermission(Manifest.permission.READ_MEDIA_IMAGES) -> {
                    if (isViewDestroyed) return
                    launchImagePicker()
                }

                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                }
            }
        } else {
            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                if (isViewDestroyed) return
                launchImagePicker()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun launchImagePicker() {
        getContent.launch("image/*")
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
        vel?.let {
            dbr?.removeEventListener(it)
        }
    }
}