package uz.isystem.newsapplication.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class RoomArticles(

    @ColumnInfo("image") val image:String?,
    @ColumnInfo("author") val author:String?,
    @ColumnInfo("title") val title:String,
    @ColumnInfo("description") val description:String?,
    @ColumnInfo("publishedAt") val publishedAt:String?,
    @ColumnInfo("content") val content:String?,
    @PrimaryKey
    @ColumnInfo("url") val url:String,
)