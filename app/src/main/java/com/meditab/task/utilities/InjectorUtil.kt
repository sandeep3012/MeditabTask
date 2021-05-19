package com.meditab.task.utilities

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.meditab.task.remote.RetrofitService
import com.meditab.task.remote.repository.RetrofitRepository
import com.meditab.task.viewmodel.ImageViewModelFactory

object InjectorUtil {
    private val gsonInstance = GsonBuilder()
        .create()

    fun getGsonInstance(): Gson = gsonInstance

    fun provideImageViewModelFactory(application: Application): ImageViewModelFactory {
        val retrofitRepository = getRetrofitRepository(application)
        return ImageViewModelFactory(application,retrofitRepository)
    }

    fun getRetrofitRepository(context: Context): RetrofitRepository {
        return RetrofitRepository.getInstance(RetrofitService.getInstance(context.applicationContext)
        )
    }
}