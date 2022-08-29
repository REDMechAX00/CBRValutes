package com.redmechax00.parsexml

data class ValuteXmlData(
    var attrID: String = "",
    var charCode: String = "",
    var name: String = "",
    var date: String = "",
    var value: String = "",
    var nominal: String = "",
    var numCode: String = ""
) {
    override fun toString(): String {
        return "Char code = $charCode\nName = ${localeValuteName(name!!)}\nValue = $value\nNominal = $nominal"
    }
}