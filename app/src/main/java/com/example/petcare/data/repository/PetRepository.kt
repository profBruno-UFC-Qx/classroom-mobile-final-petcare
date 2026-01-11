package com.example.petcare.data.repository

import com.example.petcare.data.local.dao.PetDao
import com.example.petcare.data.local.entity.PetEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repositório responsável por gerenciar as operações de dados para os Pets.
 * Ele atua como um intermediário entre a UI/ViewModels e a fonte de dados (PetDao).
 *
 * @param petDao O objeto de acesso a dados (DAO) para a entidade Pet.
 */
class PetRepository(private val petDao: PetDao) {

    /**
     * Retorna um Flow que emite uma lista de todos os pets do banco de dados.
     * O Flow garante que qualquer mudança na tabela 'pets' será refletida automaticamente
     * para quem estiver observando este fluxo.
     */
    fun getAllPets(): Flow<List<PetEntity>> {
        return petDao.getAllPets()
    }

    /**
     * Retorna um Flow que emite os dados de um pet específico, buscado pelo seu ID.
     *
     * @param petId O ID do pet a ser buscado.
     */
    fun getPetById(petId: Long): Flow<PetEntity> {
        return petDao.getPetById(petId)
    }

    /**
     * Insere ou atualiza um pet no banco de dados.
     * Por usar OnConflictStrategy.REPLACE no DAO, se um pet com o mesmo ID já existir,
     * ele será substituído.
     *
     * @param pet O objeto PetEntity a ser salvo.
     */
    suspend fun savePet(pet: PetEntity) {
        petDao.insertPet(pet)
    }

}
