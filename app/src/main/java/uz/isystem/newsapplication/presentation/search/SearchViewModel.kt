package uz.isystem.newsapplication.presentation.search

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
class SearchViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private var pageEverything = 1

    private val successDataEvery: MutableLiveData<EverythingResponse?> =
        MutableLiveData<EverythingResponse?>()
    val successResponseEvery: LiveData<EverythingResponse?>
        get() = successDataEvery

    private val errorDataEvery: MutableLiveData<String?> = MutableLiveData<String?>()
    val errorResponseEvery: LiveData<String?>
        get() = errorDataEvery


    fun search(
        q: String,
        lang: String,
        sortBy: String,
        searchIn: String,
        from: String,
        to: String
    ) {

        viewModelScope.launch {
            when (val result = repository.search(
                lang = lang,
                key = Constants.API_KEY,
                page = pageEverything,
                pageSize = 20,
                sortBy = sortBy,
                searchIn = searchIn,
                from = from,
                to = to,
                q = q

            )) {
                is ResultWrapper.Success -> {
                    successDataEvery.postValue(result.data)
                    pageEverything++
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
}