package uz.isystem.newsapplication.presentation.main.saved

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import uz.isystem.newsapplication.R
import uz.isystem.newsapplication.data.room.RoomArticles
import uz.isystem.newsapplication.data.room.RoomDataBase
import uz.isystem.newsapplication.databinding.PageSaveBinding
import uz.isystem.newsapplication.presentation.adapter.SavedAdapter
import uz.isystem.newsapplication.presentation.base.BaseFragment
import uz.isystem.newsapplication.presentation.extations.changeScreen
import uz.isystem.newsapplication.presentation.main.MainScreenDirections

class SavePage : BaseFragment(R.layout.page_save) {
    private val binding by viewBinding(PageSaveBinding::bind)
    private val adapter = SavedAdapter()
    private lateinit var auth: FirebaseAuth
    private lateinit var dbr: DatabaseReference
    private lateinit var uid: String
    private var vel: ValueEventListener? = null
    private var isViewDestroyed = false
    private val room = RoomDataBase.getInstance()

    override fun onCreate(view: View, savedInstanceState: Bundle?) {
        setAdapter()
        checkInternet()
        setActions()
    }

    private fun checkInternet() {
        if (isNetworkAvailable(requireContext())) {
            getFirebaseData()
        } else {
            setRoomData()
        }

    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


    private fun getFirebaseData() {
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid ?: ""
        dbr = FirebaseDatabase.getInstance().getReference(getString(R.string.path_users)).child(uid)
            .child(getString(R.string.path_saved))

        val articlesList = ArrayList<RoomArticles>()

        vel = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (articleSnapshot in snapshot.children) {
                    val imageUrl = articleSnapshot.child("image").getValue(String::class.java)
                    val url = articleSnapshot.child("url").getValue(String::class.java)
                    val title = articleSnapshot.child("title").getValue(String::class.java)
                    val description =
                        articleSnapshot.child("description").getValue(String::class.java)
                    val publishedAt =
                        articleSnapshot.child("publishedAt").getValue(String::class.java)
                    val author = articleSnapshot.child("author").getValue(String::class.java)
                    val content = articleSnapshot.child("content").getValue(String::class.java)

                    val article = RoomArticles(
                        image = imageUrl,
                        url = url ?: "",
                        title = title ?: "",
                        description = description,
                        publishedAt = publishedAt,
                        author = author,
                        content = content
                    )
                    articlesList.add(article)
                }
                room!!.deleteAllNews()
                room.addAllNews(articlesList)

                if (articlesList.isEmpty()) {
                    Log.d("TAGData", "firebase is empty: ${room.getAllSaved()}")
                    binding.empty.visibility = View.VISIBLE
                }
                binding.progressBar.visibility = View.GONE
                adapter.setData(articlesList)
            }

            override fun onCancelled(error: DatabaseError) {
                setRoomData()
            }
        }

        vel?.let {
            dbr.addValueEventListener(it)
        }

    }

    private fun setActions() {
        adapter.onClickItem = {
            findNavController().changeScreen(
                MainScreenDirections.actionMainScreenToDetailsScreen(
                    description = it.description.toString(),
                    title = it.title,
                    imageUrl = it.image.toString(),
                    url = it.url,
                    publishedAt = it.publishedAt.toString(),
                    author = it.author.toString()
                )
            )
        }
    }

    private fun setRoomData() {
        if (room!!.getAllSaved().isEmpty()) {
            binding.empty.visibility = View.VISIBLE
            Log.d("TAGData", "setRoomData is empty: ${room.getAllSaved()}")

        } else {
            Log.d("TAGData", "setRoomData is not empty: ${room.getAllSaved()}")
            adapter.setData(room.getAllSaved())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isViewDestroyed = true
        vel?.let {
            dbr.removeEventListener(it)
        }

    }

    private fun setAdapter() {
        binding.recyclerView.adapter = adapter
    }
}