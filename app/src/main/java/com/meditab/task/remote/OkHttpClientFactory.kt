package com.meditab.task.remote

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

abstract class OkHttpClientFactory {
    companion object{

        @Volatile
        private var instance: OkHttpClient? = null

        fun getInstance(context: Context): OkHttpClient {
            return instance ?: synchronized(this) {
                instance
                    ?: buildInstance(context).also { instance = it }
            }
        }

        private fun buildInstance(context: Context): OkHttpClient {
            var okHttpClientBuilder = OkHttpClient().newBuilder()
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder = okHttpClientBuilder.addInterceptor(logging)

            okHttpClientBuilder = okHttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
            return okHttpClientBuilder.build()
        }
    }
}