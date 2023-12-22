package uz.isystem.newsapplication.di.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import uz.isystem.newsapplication.data.network.NewsService
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Service {

    @[Provides Singleton]
    fun provideHomeService(retrofit: Retrofit):NewsService {
        return retrofit.create(NewsService::class.java)
    }
}