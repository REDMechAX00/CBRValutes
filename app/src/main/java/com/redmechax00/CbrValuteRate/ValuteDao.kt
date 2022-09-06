package com.redmechax00.CbrValuteRate

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface ValuteDao {

    @Insert(onConflict = REPLACE)
    fun add(valute: Valute)

    @Insert
    fun insertAll(vararg valutes: Valute)

    @Update
    fun update(valute: Valute)

    @Delete
    fun delete(valute: Valute)

    @Query("SELECT * FROM valute")
    fun getAllValutes(): List<Valute>

    // Получение итема по его идентификатору
    @Query("SELECT * FROM valute WHERE uid=(:id)")
    fun getValuteById(id: Int): Valute

    // Найти итем по имени
    @Query("SELECT * FROM valute WHERE char_code LIKE :charCode LIMIT 1")
    fun findByCharCode(charCode: String): Valute
}