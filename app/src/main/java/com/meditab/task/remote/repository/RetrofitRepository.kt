package com.meditab.task.remote.repository

import com.meditab.task.remote.RetrofitService

class RetrofitRepository private constructor(private val retrofitService : RetrofitService) {

    suspend fun getImageList(pithumbsize : Int, gpssearch : String) = retrofitService.getImageList("query",
        "pageimages","json", "thumbnail", pithumbsize, 50, "prefixsearch", gpssearch)

    companion object{
        @Volatile private var instance :RetrofitRepository?= null

        fun getInstance( retrofitService: RetrofitService)=
            instance ?: synchronized(this){
                instance ?: RetrofitRepository(retrofitService).also { instance=it }
            }
    }
}