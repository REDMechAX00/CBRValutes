package com.redmechax00.cbrvaluterate.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.redmechax00.cbrvaluterate.models.ValuteModel

@Database(entities = [ValuteModel::class], version = 1, exportSchema = false)
abstract class ValuteDatabase : RoomDatabase() {
    abstract fun getValuteDAO(): ValuteDao
}
