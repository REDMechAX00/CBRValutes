package com.redmechax00.parsexml

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader

class XmlPullParserHandler {
    private val listValutesXmlData = ArrayList<ValuteXmlData>()
    private var valuteXmlData: ValuteXmlData? = null
    private var text: String = ""
    private var newDate: String = ""

    fun parse(strXml: String): ArrayList<ValuteXmlData> {
        try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(StringReader(strXml))

            var eventType = parser.eventType

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val tagname = parser.name
                when (eventType) {
                    //когда видит стартовый тэг
                    XmlPullParser.START_TAG -> {
                        if (tagname.equals("ValCurs", ignoreCase = true)) {
                            newDate = parser.getAttributeValue(0)

                            //добавить рубль
                            val rubValute = ValuteXmlData()
                            rubValute.apply {
                                charCode = "RUB"
                                value = "1"
                                date = newDate
                            }
                            listValutesXmlData.add(rubValute)

                        } else if (tagname.equals("Valute", ignoreCase = true)) {
                            // создать новый класс валют
                            valuteXmlData = ValuteXmlData()
                            //добавляем атрибут ID
                            valuteXmlData!!.attrID = parser.getAttributeValue(0)
                        }
                    }

                    //любой текст между тегами
                    XmlPullParser.TEXT -> text = parser.text

                    //когда видит закрывающий тэг
                    XmlPullParser.END_TAG -> {
                        if (tagname.equals("NumCode", ignoreCase = true)) {
                            valuteXmlData!!.numCode = text
                        }  else if (tagname.equals("CharCode", ignoreCase = true)) {
                            valuteXmlData!!.charCode = text
                        } else if (tagname.equals("Nominal", ignoreCase = true)) {
                            valuteXmlData!!.nominal = text
                        } else if (tagname.equals("Name", ignoreCase = true)) {
                            valuteXmlData!!.name = text
                        } else if (tagname.equals("Value", ignoreCase = true)) {
                            valuteXmlData!!.value = text
                        } else if (tagname.equals("Valute", ignoreCase = true)) {
                            //добавить объект валюты в лист
                            valuteXmlData!!.date = newDate
                            valuteXmlData?.let { listValutesXmlData.add(it) }
                        } else if (tagname.equals("ValCurs", ignoreCase = true)) {

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