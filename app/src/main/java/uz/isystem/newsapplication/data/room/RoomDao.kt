package uz.isystem.newsapplication.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDao {
    @Query("SELECT * FROM news")
    fun getAllSaved(): List<RoomArticles>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNews(data: RoomArticles)

    @Query("SELECT url FROM news")
    fun getAllUrl():List<String>

    @Query("DELETE FROM news WHERE url=:id")
    fun deleteByUrl(id:String)

    @Query("SELECT * FROM news WHERE url = :id")
    fun getNewsByUrl(id: String): RoomArticles
}