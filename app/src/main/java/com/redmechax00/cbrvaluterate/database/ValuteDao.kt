package com.redmechax00.cbrvaluterate.database

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.redmechax00.cbrvaluterate.models.ValuteModel

@Dao
interface ValuteDao {

    @Insert(onConflict = REPLACE)
    fun add(valute: ValuteModel)

    @Insert
    fun insertAll(vararg valutes: ValuteModel)

    @Update
    fun update(valute: ValuteModel)

    @Delete
    fun delete(valute: ValuteModel)

    @Query("SELECT * FROM valuteModel")
    fun getAllValutes(): List<ValuteModel>

    // Получение итема по его идентификатору
    @Query("SELECT * FROM valuteModel WHERE uid=(:id)")
    fun getValuteById(id: Int): ValuteModel

    // Найти итем по имени
    @Query("SELECT * FROM valuteModel WHERE char_code LIKE :charCode LIMIT 1")
    fun findByCharCode(charCode: String): ValuteModel
}