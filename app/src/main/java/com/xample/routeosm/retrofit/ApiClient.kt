package com.xample.routeosm.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {

    private val TAG = "ApiClient"
    private var mRetrofit: Retrofit? = null

    val client: Retrofit
        get() {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                mRetrofit = Retrofit.Builder()
                    .baseUrl("https://routing.openstreetmap.de/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(
                        OkHttpClient.Builder()
                            .connectTimeout(2, TimeUnit.MINUTES)
                            .writeTimeout(2, TimeUnit.MINUTES)
                            .readTimeout(2, TimeUnit.MINUTES)
                            .addInterceptor(logging)
                            .build()
                    )
                    .build()
            return mRetrofit!!
        }

}