package com.example.petcare.utils

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    /**
     * Converte um valor Long (timestamp em milissegundos) do banco de dados
     * para um objeto Date que o aplicativo pode usar.
     * O Room usa esta função ao ler dados.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        // Se o valor do banco não for nulo, cria um Date a partir dele.
        return value?.let { Date(it) }
    }

    /**
     * Converte um objeto Date do aplicativo para um valor Long (timestamp)
     * que pode ser armazenado no banco de dados.
     * O Room usa esta função ao salvar dados.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        // Se o objeto Date não for nulo, pega o seu valor em milissegundos.
        return date?.time
    }
}
