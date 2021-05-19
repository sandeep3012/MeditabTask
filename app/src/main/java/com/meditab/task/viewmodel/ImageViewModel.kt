package com.meditab.task.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.meditab.task.remote.Resource
import com.meditab.task.remote.entity.PagesInfo
import com.meditab.task.remote.entity.ResponseEntity
import com.meditab.task.remote.repository.RetrofitRepository
import com.meditab.task.utilities.InjectorUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject


class ImageViewModel internal constructor(
    application: Application,
    val retrofitRepository: RetrofitRepository
) : AndroidViewModel(application) {

    var liveSearchResponse = MutableLiveData<ResponseEntity?>()

    val imageSearchResponse: MutableLiveData<Resource<ResponseEntity?>> =
        MutableLiveData(Resource.inactive(null))

    fun searchImage(pithumbsize: Int = 200, gpssearch: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                imageSearchResponse.postValue(Resource.loading<ResponseEntity?>(null))
                val response = retrofitRepository.getImageList(pithumbsize, gpssearch)
                if (response.isSuccessful) {
                    try {
                        val jsonObjStr : String = response.body().toString()
                        val jsonObj = JSONObject(jsonObjStr)
                        val batchcomplete : String? = jsonObj.getString("batchcomplete")
                        val query = jsonObj.getJSONObject("query")
                        val page = query.getJSONObject("pages")
                        val x: Iterator<*> = page.keys()
                        val pageArray = JSONArray()
                        while (x.hasNext()){
                            val key = x.next() as String
                            pageArray.put(page.get(key))
                        }
                        val pageItems = mutableListOf<PagesInfo>()

                        for (i in 0 until pageArray.length()){
                            val pageInfoObj =pageArray.get(i).toString()
                            val gson = Gson()
                            pageItems.add(gson.fromJson(pageInfoObj, PagesInfo::class.java))
                        }
                        val searchReponse = ResponseEntity(batchcomplete = batchcomplete, pagesInfo = pageItems)
                        imageSearchResponse.postValue(Resource.success(searchReponse))
                    } catch (e: Throwable){
                        imageSearchResponse.postValue(
                            Resource.error(
                                "Failed to connect to server. Do you have internet connectivity?",
                                null
                            )
                        )
                    }

                } else {
                    val errorBody = response.errorBody()?.string()

                    if (!errorBody.isNullOrEmpty()) {
                        val errorJson = try {
                            InjectorUtil.getGsonInstance()
                                .fromJson(errorBody, JsonObject::class.java)
                        } catch (e: Exception) {
                            JsonNull.INSTANCE
                        }
                        if (errorJson is JsonObject && errorJson.has("error")) {

                        } else {
                            imageSearchResponse.postValue(
                                Resource.error<ResponseEntity?>(
                                    "${response.code()} - $errorBody",
                                    null
                                )
                            )
                        }
                    } else {
                        imageSearchResponse.postValue(
                            Resource.error<ResponseEntity?>(
                                "${response.code()} - Unknown Error",
                                null
                            )
                        )
                    }
                }
            } catch (e: Throwable) {
                imageSearchResponse.postValue(
                    Resource.error(
                        "Failed to connect to server. Do you have internet connectivity?",
                        null
                    )
                )
            }

        }
    }
}