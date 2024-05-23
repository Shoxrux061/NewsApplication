package uz.isystem.newsapplication.domain.repository

import kotlinx.coroutines.Dispatchers
import uz.isystem.newsapplication.data.model.everything.EverythingResponse
import uz.isystem.newsapplication.data.network.NewsService
import uz.isystem.newsapplication.utills.ResultWrapper
import uz.isystem.newsapplication.utills.parseResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(private val service: NewsService) {
    suspend fun getEverything(
        apiKey: String,
        page: Int,
        pageSize: Int,
        lang: String,
        sortBy: String,
        q: String,
        searchIn: String
    ): ResultWrapper<EverythingResponse?, Any?> {
        return parseResponse(Dispatchers.IO) {
            service.getLastNews(
                apiKey = apiKey,
                lang = lang,
                sort = sortBy,
                pageSize = pageSize,
                page = page,
                q = q,
                searchIn = searchIn
            )
        }
    }

    suspend fun getCategories(
        key: String,
        page: Int,
        pageSize: Int,
        lang: String,
        category: String,
    ): ResultWrapper<EverythingResponse?, Any?> {
        return parseResponse(Dispatchers.IO) {
            service.getCategories(
                key = key,
                page = page,
                pageSize = pageSize,
                lang = lang,
                category = category
            )
        }
    }

    suspend fun search(
        key: String,
        lang: String,
        page: Int,
        pageSize: Int,
        searchIn: String,
        sortBy: String,
        from : String,
        to : String,
        q:String
    ): ResultWrapper<EverythingResponse?, Any?> {
        return parseResponse(Dispatchers.IO) {
            service.search(
                key = key,
                lang = lang,
                page = page,
                pageSize = pageSize,
                sortBy = sortBy,
                searchIn = searchIn,
                from = from,
                to = to, q = q
            )
        }
    }
}