package com.example.petcare.data.repository

import com.example.petcare.data.remote.api.PetShopApiService

/**
 * Repositório responsável por buscar dados relacionados a pet shops.
 * Atua como uma ponte entre o ViewModel e a fonte de dados remota (API).
 *
 * @param apiService A instância do serviço Retrofit que faz as chamadas de rede.
 */
class PetShopRepository(private val apiService: PetShopApiService) {

    /**
     * Chama o serviço da API para buscar os pet shops com base em uma query Overpass.
     * @param query A string de consulta no formato Overpass QL.
     * @return A resposta da API.
     */
    suspend fun getPetShops(query: String) = apiService.getPetShops(query)

}
