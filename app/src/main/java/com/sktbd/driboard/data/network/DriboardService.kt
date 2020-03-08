package com.sktbd.driboard.data.network

import com.sktbd.driboard.data.model.Draft
import com.sktbd.driboard.data.model.Shot
import com.sktbd.driboard.data.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.io.File
interface DriboardService {
    @GET("user/")
    fun getUser(@Query("access_token") access_token:String): Call<User>

    @GET("user/shots")
    fun getUserShots(@Query("access_token") access_token:String): Call<List<Shot>>

    @GET("shots/{id}")
    fun getShot(
        @Header("Authorization") authorization:String,
        @Path(value = "id", encoded = true) id: String
    ): Call<Draft>


    @POST("shots/")
    fun publishShot(
        @Header("Authorization") authorization:String,
        @Body body: RequestBody
        ): Call<Response<Void>>

    @FormUrlEncoded
    @PUT("shots/{id}")
    fun updateShot(
        @Header("Authorization") authorization:String,
        @Path(value = "id", encoded = true) id: String,
        @Field("title") title: String,
        @Field("description") description: String?,
        @Field("tags") tags: String?
    ): Call<Response<Void>>
}