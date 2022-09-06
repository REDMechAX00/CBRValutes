package com.redmechax00.cbrvaluterate.utilits

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.redmechax00.cbrvaluterate.R
import com.redmechax00.cbrvaluterate.models.ValuteModel
import com.redmechax00.cbrvaluterate.models.ValuteXmlDataModel
import com.redmechax00.cbrvaluterate.recyclerview.ValuteViewModel
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
fun parse(
    urlString: String,
    onSuccess: (listOfValutes: ArrayList<ValuteXmlDataModel>) -> Unit
): Boolean {
    if (isNetworkAvailable()) {
        val result: Deferred<String?> = GlobalScope.async {
            downloadXml(urlString)
        }

        GlobalScope.launch(Dispatchers.Main) {
            result.await()?.let { stringXml ->
                parseXML(stringXml) { listOfXmlValutes ->
                    onSuccess(listOfXmlValutes)
                }
            }
        }
        return true
    } else {
        showToast("Network error")
        return false
    }

}

private fun downloadXml(urlString: String): String? {
    var stringXml: String? = null

    val url = URL(urlString)
    val urlConnection = url.openConnection() as HttpURLConnection
    urlConnection.readTimeout = 100000
    urlConnection.connectTimeout = 100000
    urlConnection.requestMethod = "GET"
    urlConnection.instanceFollowRedirects = true
    urlConnection.useCaches = false
    urlConnection.doInput = true

    val responseCode: Int = urlConnection.responseCode
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

private fun parseXML(
    inStream: String,
    onSuccess: (listOfXmlValutes: ArrayList<ValuteXmlDataModel>) -> Unit
) {
    try {
        val parser = XmlPullParserHandler()
        val listOfXmlValutes: ArrayList<ValuteXmlDataModel> = parser.parse(inStream)
        onSuccess(listOfXmlValutes)

    } catch (e: IOException) {
        e.printStackTrace()
    }
}

fun updateData(
    viewModel: ValuteViewModel,
    newData: ArrayList<ValuteXmlDataModel>,
    onSuccess: (updatedData: ArrayList<ValuteModel>) -> Unit
) {
    viewModel.getListValutes().value?.let { mainData ->
        mainData.forEach { valute ->
            val charCode = valute.charCode
            for (i in 0 until newData.size) {
                when (charCode) {
                    newData[i].charCode -> {
                        valute.value = newData[i].value
                        valute.date = newData[i].date
                        valute.nominal = newData[i].nominal
                    }
                }
            }
        }
        onSuccess(mainData)
    }
}

fun showToast(text: String) {
    Toast.makeText(APP_ACTIVITY, text, Toast.LENGTH_SHORT).show()
}

fun getTodayDate(today: Calendar): String {
    val year = today.get(Calendar.YEAR)
    val month = today.get(Calendar.MONTH) + 1
    val day = today.get(Calendar.DAY_OF_MONTH)

    val dd: String = if ((day) - (day) % 10 == 0) {
        "0${day}"
    } else {
        (day).toString()
    }
    val mm: String = if ((month) - (month) % 10 == 0) {
        "0${month}"
    } else {
        (month).toString()
    }

    return "$dd/$mm/$year"
}

fun getValuteSign(charCode: String): String {
    when (charCode) {
        APP_ACTIVITY.getString(R.string.valute_char_code_aud) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_aud)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_azn) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_azn)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_gbp) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_gbp)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_amd) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_amd)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_rub) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_rub)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_byn) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_byn)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_bgn) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_bgn)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_brl) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_brl)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_huf) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_huf)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_hkd) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_hkd)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_dkk) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_dkk)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_usd) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_usd)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_eur) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_eur)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_inr) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_inr)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_kzt) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_kzt)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_kgs) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_kgs)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_cny) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_cny)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_mdl) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_mdl)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_nok) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_nok)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_pln) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_pln)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_ron) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_ron)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_xdr) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_xdr)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_sgd) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_sgd)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_tjs) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_tjs)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_try) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_try)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_tmt) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_tmt)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_uzs) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_uzs)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_czk) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_czk)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_sek) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_sek)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_chf) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_chf)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_zar) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_zar)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_krw) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_krw)
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_jpy) -> {
            return APP_ACTIVITY.getString(R.string.valute_sign_jpy)
        }
        else -> {
            return ""
        }
    }
}

fun getValuteIcon(charCode: String): Drawable? {
    when (charCode) {
        APP_ACTIVITY.getString(R.string.valute_char_code_aud) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_australia
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_azn) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_azerbaijan
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_gbp) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_great_britain
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_amd) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_armenia
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_rub) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_russian_federation
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_byn) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_belarus
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_bgn) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_bulgaria
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_brl) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_brazil
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_huf) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_hungary
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_hkd) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_hongkong
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_dkk) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_denmark
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_usd) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_usa
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_eur) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_europe
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_inr) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_india
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_kzt) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_kazakhstan
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_kgs) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_kyrgyzstan
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_cny) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_china
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_mdl) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_moldova_circular
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_nok) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_norway
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_pln) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_poland
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_ron) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_romania
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_xdr) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_valute_unknown
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_sgd) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_singapore
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_tjs) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_tajikistan
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_try) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_turkey
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_tmt) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_turkmenistan_circular
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_uzs) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_uzbekistan
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_czk) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_czech_republic
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_sek) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_sweden
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_chf) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_switzerland
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_zar) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_south_africa
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_krw) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_south_korea
            )
        }
        APP_ACTIVITY.getString(R.string.valute_char_code_jpy) -> {
            return AppCompatResources.getDrawable(
                APP_ACTIVITY,
                R.drawable.ic_japan
            )
        }
        else -> {
            return null
        }
    }
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