package com.redmechax00.CbrValuteRate

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Valute::class], version = 1, exportSchema = false)
abstract class ValuteDatabase : RoomDatabase() {
    abstract fun getValuteDAO(): ValuteDao
}
