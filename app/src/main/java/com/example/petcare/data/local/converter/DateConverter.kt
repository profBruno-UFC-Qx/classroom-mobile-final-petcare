package com.example.petcare.data.local.converter

import androidx.room.TypeConverter
import java.util.Date

/**
 * TypeConverter para o Room saber como lidar com o tipo Date.
 * Ele converte um Date para um Long (timestamp) para salvar no banco de dados,
 * e converte o Long de volta para um Date ao ler do banco de dados.
 */
class DateConverter {

    /**
     * Converte um Long (timestamp) para um objeto Date.
     * O valor do banco de dados pode ser nulo.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        // Se o valor for nulo, retorna nulo. Senão, cria um novo Date.
        return value?.let { Date(it) }
    }

    /**
     * Converte um objeto Date para um Long (timestamp).
     * O objeto Date que vem do código pode ser nulo.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        // Se a data for nula, retorna nulo. Senão, retorna o tempo em milissegundos.
        return date?.time
    }
}
