package com.meditab.task.remote

import android.content.Context
import com.google.gson.JsonObject
import com.meditab.task.remote.entity.ResponseEntity
import com.meditab.task.utilities.InjectorUtil
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {

    @GET("/w/api.php")
    suspend fun getImageList(
        @Query("action") action: String?,
        @Query("prop") prop: String?,
        @Query("format") format: String?,
        @Query("piprop") piprop: String?,
        @Query("pithumbsize") pithumbsize: Int?,
        @Query("pilimit") pilimit: Int?,
        @Query("generator") generator: String?,
        @Query("gpssearch") gpssearch: String?
    ) : retrofit2.Response<JsonObject>

    companion object {
        // For Singleton instantiation
        @Volatile
        private var instance: RetrofitService? = null

        fun getInstance(context: Context): RetrofitService {
            return instance ?: synchronized(this) {
                instance
                    ?: createInstance(context).also { instance = it }
            }
        }

        private fun createInstance(context: Context): RetrofitService {
            return retrofit(context).create(RetrofitService::class.java)
        }

        private fun retrofit(context: Context): Retrofit {
            return Retrofit.Builder()
                .client(OkHttpClientFactory.getInstance(context))
                .baseUrl("https://en.wikipedia.org")
                .addConverterFactory(GsonConverterFactory.create(InjectorUtil.getGsonInstance()))
                .build()
        }
    }

}

