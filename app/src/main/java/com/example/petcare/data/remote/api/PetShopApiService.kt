package com.example.petcare.data.remote.api

import retrofit2.http.Body
import retrofit2.http.POST

interface PetShopApiService {
    /**
     * Faz uma requisição para a API Overpass.
     * @param query A string de consulta no formato Overpass QL.
     * @return Uma resposta Overpass contendo a lista de elementos (pet shops).
     */
    @POST("interpreter")
    suspend fun getPetShops(@Body query: String): OverpassResponse
}
