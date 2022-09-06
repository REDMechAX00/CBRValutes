package com.redmechax00.CbrValuteRate

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class ValuteViewModel : ViewModel() {

    private val valutesList: MutableLiveData<ArrayList<Valute>> = MutableLiveData()

    init {
        val db = Room.databaseBuilder(
            APP_ACTIVITY,
            ValuteDatabase::class.java, "database-valutes"
        ).build()
        val valuteDao = db.getValuteDAO()
        val result: Deferred<List<Valute>> = GlobalScope.async {
            return@async valuteDao.getAllValutes()
        }

        GlobalScope.launch(Dispatchers.Main) {
            if (result.await().isNotEmpty()) {
                //Set icons for items
                result.await().forEach {
                    it.icon = getValuteIcon(it.charCode)
                }
                valutesList.value = ArrayList(result.await())
            } else {
                valutesList.value = ValuteObject.getUsers()
            }
        }
    }

    fun getListValutes() = valutesList

}