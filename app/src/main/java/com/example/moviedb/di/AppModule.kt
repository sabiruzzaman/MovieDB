package com.example.moviedb.di

import android.content.Context
import com.example.moviedb.data.repository.StockCsvRepository
import com.example.moviedb.data.source.local.MovieDbDatabase
import com.example.moviedb.data.source.remote.ApiInterface
import com.example.moviedb.data.source.remote.AuthInterceptor
import com.example.moviedb.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        interceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, (5 * 1024 * 1024).toLong()))
            .addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiInterface(retrofitBuilder: Retrofit.Builder, client: OkHttpClient): ApiInterface {
        return retrofitBuilder.client(client).build().create(ApiInterface::class.java)
    }


    @Provides
    @Singleton
    fun provideCsvRepository(@ApplicationContext context: Context): StockCsvRepository {
        return StockCsvRepository(context)
    }

    @Provides
    @Singleton
    fun provideDao(@ApplicationContext context: Context) =
        MovieDbDatabase.invoke(context).getSavedMoviesDao()

}