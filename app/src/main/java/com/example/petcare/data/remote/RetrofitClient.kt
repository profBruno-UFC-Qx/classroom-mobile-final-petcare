package com.example.petcare.data.remote

import com.example.petcare.data.remote.api.PetShopApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    // URL base da API pública do Overpass
    private const val BASE_URL = "https://overpass-api.de/api/"

    // Cliente HTTP que nos permite adicionar cabeçalhos, como o 'Content-Type'
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    // Instância do Retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Adiciona dois conversores:
            // 1. ScalarsConverterFactory para lidar com a query String no Body
            // 2. GsonConverterFactory para converter a resposta JSON para nossos data-classes
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient) // Usa o cliente HTTP configurado
            .build()
    }

    // Expõe o serviço da API para ser usado pelo repositório
    val petShopApiService: PetShopApiService by lazy {
        retrofit.create(PetShopApiService::class.java)
    }
}