package com.meditab.task.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meditab.task.remote.repository.RetrofitRepository

class ImageViewModelFactory(val application: Application, private val retrofitRepository: RetrofitRepository)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImageViewModel(application, retrofitRepository) as T
    }
}