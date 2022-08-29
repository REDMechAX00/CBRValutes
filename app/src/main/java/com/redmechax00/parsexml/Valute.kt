package com.redmechax00.parsexml

import android.graphics.drawable.Drawable

data class Valute(
    var position: Int = 0,
    var charCode: String = "",
    var name: String = "",
    var icon: Drawable? = null,
    var value: String = "",
    var date: String = "",
    var nominal: String = ""
)
