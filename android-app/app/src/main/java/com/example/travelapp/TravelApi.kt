package com.example.travelapp


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// datos de FastApi
data class TravelRequest(
    val popularidad: String,
    val estacion:    String,
    val rating:      String,
    val precio:      String,
    val clima:       String,
    val idioma:      String,
    val continente:  String
)
data class TravelResponse(val recomendaciones: List<String>)

// Interfaz Retrofit
interface TravelApi {
    @POST("predict")
    suspend fun predict(@Body req: TravelRequest): TravelResponse
}

// Singleton de cliente Retrofit
object ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.23.37:8000/")  // sustituye por tu IP LAN
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val travelApi: TravelApi = retrofit.create(TravelApi::class.java)
}
