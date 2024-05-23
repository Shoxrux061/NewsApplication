package uz.isystem.newsapplication.presentation.seeAll

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import uz.isystem.newsapplication.data.model.everything.EverythingResponse
import uz.isystem.newsapplication.domain.repository.NewsRepository
import uz.isystem.newsapplication.utills.Constants
import uz.isystem.newsapplication.utills.ResultWrapper
import javax.inject.Inject

@HiltViewModel
class SeeAllViewModel @Inject constructor(
    private val repository: NewsRepository,
    @ApplicationContext context: Context
) : ViewModel() {


    private val successDataEvery: MutableLiveData<EverythingResponse?> =
        MutableLiveData<EverythingResponse?>()
    val successResponseEvery: LiveData<EverythingResponse?>
        get() = successDataEvery

    private val errorDataEvery: MutableLiveData<String?> = MutableLiveData<String?>()
    val errorResponseEvery: LiveData<String?>
        get() = errorDataEvery


    private val successDataCategory: MutableLiveData<EverythingResponse?> =
        MutableLiveData<EverythingResponse?>()
    val successResponseCategory: LiveData<EverythingResponse?>
        get() = successDataCategory

    private val errorDataCategory: MutableLiveData<String?> = MutableLiveData<String?>()
    val errorResponseCategory: LiveData<String?>
        get() = errorDataCategory


    fun getEverything(lang: String) {

        viewModelScope.launch {
            when (val result = repository.getEverything(
                q = "a",
                lang = lang,
                apiKey = Constants.API_KEY,
                page = 1,
                pageSize = 20,
                sortBy = "publishedAt",
                searchIn = "title"
            )) {
                is ResultWrapper.Success -> {
                    successDataEvery.postValue(result.data)
                }

                is ResultWrapper.Error -> {
                    errorDataEvery.postValue(result.message.toString())
                }

                is ResultWrapper.NetworkError -> {
                    errorDataEvery.postValue("No internet")
                }
            }
        }
    }

    fun getCategories(lang: String, category: String) {
        viewModelScope.launch {
            when (val result = repository.getCategories(
                lang = lang,
                key = Constants.API_KEY,
                page = 1,
                pageSize = 20,
                category = category

            )) {
                is ResultWrapper.Success -> {
                    successDataCategory.postValue(result.data)
                }

                is ResultWrapper.Error -> {
                    errorDataCategory.postValue(result.message.toString())
                }

                is ResultWrapper.NetworkError -> {
                    errorDataCategory.postValue("No internet")
                }
            }
        }
    }
}