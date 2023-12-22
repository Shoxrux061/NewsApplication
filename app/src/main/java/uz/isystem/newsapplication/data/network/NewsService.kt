package uz.isystem.newsapplication.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.isystem.newsapplication.data.model.everything.EverythingResponse

interface NewsService {
    @GET("v2/everything")
    suspend fun getLastNews(
        @Query("q")q:String,
        @Query("searchIn")searchIn:String,
        @Query("apiKey") apiKey: String,
        @Query("language") lang: String,
        @Query("sortBy") sort: String,
        @Query("pageSize") pageSize : Int,
        @Query("page") page : Int
    ): Response<EverythingResponse?>

    @GET("v2/top-headlines")
    suspend fun getCategories(
        @Query("category")category:String,
        @Query("apiKey")key:String,
        @Query("language")lang: String,
        @Query("pageSize")pageSize: Int,
        @Query("page")page: Int
    ):Response<EverythingResponse?>
}