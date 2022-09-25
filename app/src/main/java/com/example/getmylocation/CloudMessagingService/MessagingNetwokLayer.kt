package com.example.getmylocation.CloudMessagingService

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL = "https://fcm.googleapis.com/"
private const val SERVER_KEY = "YOUR_AUTHORIZATION_KEY_GOES_HERE"
private const val CONTENT_TYPE = "application/json"


val client = OkHttpClient.Builder().addInterceptor {
    val request = it.request().newBuilder()
        .header("Authorization", "key=$SERVER_KEY")
        .header("Content-Type", CONTENT_TYPE)
        .build()
    it.proceed(request)
}.build()

val retorfit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()


interface MessagingAPi {
    @POST("fcm/send")
    suspend fun pushNotification(@Body pn: NewUserPushNotification):
            Response<ResponseBody>

}


object sendNotificationOb {
    val sendNotification: MessagingAPi by lazy {
        retorfit.create(MessagingAPi::class.java)
    }
}