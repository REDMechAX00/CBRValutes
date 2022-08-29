package com.redmechax00.parsexml

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {

    var userList : MutableLiveData<List<Valute>> = MutableLiveData()

    //инициализируем список и заполняем его данными пользователей
    init {
        userList.value = ValuteObject.getUsers()
    }

    fun getListUsers() = userList
}