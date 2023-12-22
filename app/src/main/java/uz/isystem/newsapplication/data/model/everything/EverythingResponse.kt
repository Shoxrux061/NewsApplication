package uz.isystem.newsapplication.data.model.everything


import com.google.gson.annotations.SerializedName

data class EverythingResponse(
    @SerializedName("articles")
    val articles: List<Article>,
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)