package com.redmechax00.cbrvaluterate.utilits

import com.redmechax00.cbrvaluterate.models.ValuteXmlDataModel
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

class XmlPullParserHandler {
    private val listValutesXmlData = ArrayList<ValuteXmlDataModel>()
    private var valuteXmlData: ValuteXmlDataModel? = null
    private var text: String = ""
    private var newDate: String = ""

    fun parse(strXml: String): ArrayList<ValuteXmlDataModel> {
        try {
            println(strXml)
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(StringReader(strXml))

            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (eventType) {
                    //когда видит стартовый тэг
                    XmlPullParser.START_TAG -> {
                        if (tagName.equals("ValCurs", ignoreCase = true)) {
                            newDate = parser.getAttributeValue(0)

                        } else if (tagName.equals("Valute", ignoreCase = true)) {
                            // создать новый класс валют
                            valuteXmlData = ValuteXmlDataModel()
                            //добавляем атрибут ID
                            valuteXmlData!!.attrID = parser.getAttributeValue(0)
                        }
                    }

                    //любой текст между тегами
                    XmlPullParser.TEXT -> text = parser.text

                    //когда видит закрывающий тэг
                    XmlPullParser.END_TAG -> {
                        if (tagName.equals("NumCode", ignoreCase = true)) {
                            valuteXmlData!!.numCode = text
                        } else if (tagName.equals("CharCode", ignoreCase = true)) {
                            valuteXmlData!!.charCode = text
                        } else if (tagName.equals("Nominal", ignoreCase = true)) {
                            valuteXmlData!!.nominal = text
                        } else if (tagName.equals("Name", ignoreCase = true)) {
                            valuteXmlData!!.name = text
                        } else if (tagName.equals("Value", ignoreCase = true)) {
                            valuteXmlData!!.value = text
                        } else if (tagName.equals("Valute", ignoreCase = true)) {
                            //добавить объект валюты в лист
                            valuteXmlData?.let {
                                it.date = newDate
                                listValutesXmlData.add(it)
                            }
                        }
                    }
                    else -> {}
                }
                eventType = parser.next()
            }

        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listValutesXmlData
    }
}