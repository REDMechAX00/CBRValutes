package com.redmechax00.cbrvaluterate.models

import com.redmechax00.cbrvaluterate.utilits.localeValuteName

data class ValuteXmlDataModel(
    var attrID: String = "",
    var charCode: String = "",
    var name: String = "",
    var date: String = "",
    var value: String = "",
    var nominal: String = "",
    var numCode: String = ""
) {
    override fun toString(): String {
        return "Char code = $charCode\nName = ${localeValuteName(name)}\nValue = $value\nNominal = $nominal"
    }
}