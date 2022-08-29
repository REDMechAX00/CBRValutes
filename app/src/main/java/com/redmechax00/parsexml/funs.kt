package com.redmechax00.parsexml

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import kotlinx.coroutines.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

fun isNetworkAvailable(): Boolean {
    val connectivityManager =
        APP_ACTIVITY.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
            return true
        }
    }
    return false
}

@OptIn(DelicateCoroutinesApi::class)
fun parse(urlString: String, onSuccess: (valutes: ArrayList<ValuteXmlData>) -> Unit) {
    if (isNetworkAvailable()) {
        val result: Deferred<String?> = GlobalScope.async {
            downloadXml(urlString)
        }

        GlobalScope.launch(Dispatchers.Main) {
            result.await()?.let { stringXml ->
                parseXML(stringXml) { valutes ->
                    onSuccess(valutes)
                }
            }
        }
    } else {
        showToast("Нет интернета")
    }

}

private fun downloadXml(urlString: String): String? {
    var stringXml: String? = null

    val url = URL(urlString)
    val urlConnection = url.openConnection() as HttpURLConnection
    urlConnection.setReadTimeout(100000)
    urlConnection.setConnectTimeout(100000)
    urlConnection.setRequestMethod("GET")
    urlConnection.setInstanceFollowRedirects(true)
    urlConnection.setUseCaches(false)
    urlConnection.setDoInput(true)

    val responseCode: Int = urlConnection.getResponseCode()
    if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
        try {
            stringXml = urlConnection.inputStream.bufferedReader().readText()
            return stringXml
        } finally {
            urlConnection.disconnect()
        }
    }
    return stringXml
}

fun updateDate(viewModel: UserViewModel,newData: ArrayList<ValuteXmlData>, onSucces:(updatedData: List<Valute>) -> Unit){
    var mainData: List<Valute>? = null
    viewModel.getListUsers().value?.let {
        mainData = it
    }

    mainData?.let { mainData ->
        mainData.forEachIndexed { index, valute ->
            val charCode = valute.charCode
            for (i in 0 until newData.size){
                when(charCode){
                    newData[i].charCode -> {
                        valute.value = newData[i].value
                        valute.date = newData[i].date
                    }
                }
            }
        }
        onSucces(mainData)
    }

}

private fun parseXML(
    instream: String,
    onSuccess: (valutes: ArrayList<ValuteXmlData>) -> Unit) {
    try {
        val parser = XmlPullParserHandler()
        val valutes: ArrayList<ValuteXmlData> = parser.parse(instream)

        onSuccess(valutes)

    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun showToast(text: String) {
    Toast.makeText(APP_ACTIVITY, text, Toast.LENGTH_SHORT).show()
}

fun getTodayDate(today: Calendar): String {
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH) + 1
    val day = today.get(Calendar.DAY_OF_MONTH)
    var date = "$day/$month/$year"
    if (month - month % 10 == 0) {
        date = "$day/0${month}/$year"
    }
    return date
}

fun localeValuteName(inputName: String): String {
    val outputName: String = when (inputName) {
        "Australian Dollar" -> APP_ACTIVITY.getString(R.string.valute_name_aud)
        "Azerbaijan Manat" -> APP_ACTIVITY.getString(R.string.valute_name_azn)
        "British Pound Sterling" -> APP_ACTIVITY.getString(R.string.valute_name_gbp)
        "Armenia Dram" -> APP_ACTIVITY.getString(R.string.valute_name_amd)
        "Russian Ruble" -> APP_ACTIVITY.getString(R.string.valute_name_rub)
        "Belarussian Ruble" -> APP_ACTIVITY.getString(R.string.valute_name_byn)
        "Bulgarian lev" -> APP_ACTIVITY.getString(R.string.valute_name_bgn)
        "Brazil Real" -> APP_ACTIVITY.getString(R.string.valute_name_brl)
        "Hungarian Forint" -> APP_ACTIVITY.getString(R.string.valute_name_huf)
        "Hong Kong Dollar" -> APP_ACTIVITY.getString(R.string.valute_name_hkd)
        "Danish Krone" -> APP_ACTIVITY.getString(R.string.valute_name_dkk)
        "US Dollar" -> APP_ACTIVITY.getString(R.string.valute_name_usd)
        "Euro" -> APP_ACTIVITY.getString(R.string.valute_name_eur)
        "Indian Rupee" -> APP_ACTIVITY.getString(R.string.valute_name_inr)
        "Kazakhstan Tenge" -> APP_ACTIVITY.getString(R.string.valute_name_kzt)
        "Canadian Dollar" -> APP_ACTIVITY.getString(R.string.valute_name_cad)
        "Kyrgyzstan Som" -> APP_ACTIVITY.getString(R.string.valute_name_kgs)
        "China Yuan" -> APP_ACTIVITY.getString(R.string.valute_name_cny)
        "Moldova Lei" -> APP_ACTIVITY.getString(R.string.valute_name_mdl)
        "Norwegian Krone" -> APP_ACTIVITY.getString(R.string.valute_name_nok)
        "Polish Zloty" -> APP_ACTIVITY.getString(R.string.valute_name_pln)
        "Romanian Leu" -> APP_ACTIVITY.getString(R.string.valute_name_ron)
        "SDR" -> APP_ACTIVITY.getString(R.string.valute_name_xdr)
        "Singapore Dollar" -> APP_ACTIVITY.getString(R.string.valute_name_sgd)
        "Tajikistan Ruble" -> APP_ACTIVITY.getString(R.string.valute_name_tjs)
        "Turkish Lira" -> APP_ACTIVITY.getString(R.string.valute_name_try)
        "New Turkmenistan Manat" -> APP_ACTIVITY.getString(R.string.valute_name_tmt)
        "Uzbekistan Sum" -> APP_ACTIVITY.getString(R.string.valute_name_uzs)
        "Ukrainian Hryvnia" -> APP_ACTIVITY.getString(R.string.valute_name_uah)
        "Czech Koruna" -> APP_ACTIVITY.getString(R.string.valute_name_czk)
        "Swedish Krona" -> APP_ACTIVITY.getString(R.string.valute_name_sek)
        "Swiss Franc" -> APP_ACTIVITY.getString(R.string.valute_name_chf)
        "S.African Rand" -> APP_ACTIVITY.getString(R.string.valute_name_zar)
        "South Korean Won" -> APP_ACTIVITY.getString(R.string.valute_name_krw)
        "Japanese Yen" -> APP_ACTIVITY.getString(R.string.valute_name_jpy)
        else -> {
            "Unknown"
        }
    }
    return outputName
}