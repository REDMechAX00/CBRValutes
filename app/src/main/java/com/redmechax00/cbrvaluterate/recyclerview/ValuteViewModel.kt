package com.redmechax00.cbrvaluterate.recyclerview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.redmechax00.cbrvaluterate.database.ValuteDatabase
import com.redmechax00.cbrvaluterate.models.ValuteModel
import com.redmechax00.cbrvaluterate.utilits.APP_ACTIVITY
import com.redmechax00.cbrvaluterate.utilits.getValuteIcon
import kotlinx.coroutines.*

@OptIn(DelicateCoroutinesApi::class)
class ValuteViewModel : ViewModel() {

    private val valutesList: MutableLiveData<ArrayList<ValuteModel>> = MutableLiveData()

    init {
        val db = Room.databaseBuilder(
            APP_ACTIVITY,
            ValuteDatabase::class.java, "database-valutes"
        ).build()
        val valuteDao = db.getValuteDAO()
        val result: Deferred<List<ValuteModel>> = GlobalScope.async {
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