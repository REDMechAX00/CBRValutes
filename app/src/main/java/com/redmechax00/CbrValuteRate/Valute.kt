package com.redmechax00.CbrValuteRate

import android.graphics.drawable.Drawable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Valute(
    @PrimaryKey var uid: Int,
    @ColumnInfo(name = "char_code") var charCode: String = "",
    @ColumnInfo(name = "name") var name: String = "",
    @Ignore var icon: Drawable? = null,
    @ColumnInfo(name = "value") var value: String = "",
    @ColumnInfo(name = "nominal") var nominal: String = "",
    @ColumnInfo(name = "date") var date: String = ""
)
{
    constructor() : this(0, "", "", null, "", "", "")
}