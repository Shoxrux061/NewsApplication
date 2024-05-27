package uz.isystem.newsapplication.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import uz.isystem.newsapplication.data.cache.LocaleStorage
import uz.isystem.newsapplication.data.room.RoomDataBase

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        RoomDataBase.init(this)
        RoomDataBase.getInstance()
        LocaleStorage.init(this)
    }
}