package uz.isystem.newsapplication.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.isystem.newsapplication.data.model.everything.EverythingResponse
import uz.isystem.newsapplication.domain.repository.NewsRepository
import uz.isystem.newsapplication.utills.Constants
import uz.isystem.newsapplication.utills.ResultWrapper
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository):ViewModel(){

    private var page = 1

    private val successLiveData: MutableLiveData<EverythingResponse?> = MutableLiveData<EverythingResponse?>()
    val successResponseEvery: LiveData<EverythingResponse?>
        get() = successLiveData

    private val errorLiveData: MutableLiveData<String?> = MutableLiveData<String?>()
    val errorResponseEvery: LiveData<String?>
        get() = errorLiveData


    fun getEverything(q:String,lang:String) {

        viewModelScope.launch {
            when (val result = repository.getEverything(
                q = q,
                lang = lang,
                apiKey = Constants.API_KEY,
                page = page,
                pageSize = 20,
                sortBy = "publishedAt",
                searchIn = "title"
            )) {
                is ResultWrapper.Success -> {
                    successLiveData.postValue(result.data)
                    page++
                }

                is ResultWrapper.Error -> {
                    errorLiveData.postValue(result.message.toString())
                }

                is ResultWrapper.NetworkError -> {
                    errorLiveData.postValue("No internet")
                }
            }
        }
    }
}