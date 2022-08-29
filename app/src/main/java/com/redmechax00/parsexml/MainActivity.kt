package com.redmechax00.parsexml

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.redmechax00.parsexml.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var btnParse: Button
    private lateinit var textLink: TextView
    private lateinit var datePicker: DatePicker
    private lateinit var listView: RecyclerView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var valutes_link: String
    private lateinit var metall_link: String

    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this

        btnParse = mBinding.btnParse
        textLink = mBinding.textLink
        listView = mBinding.recyclerView
        datePicker = mBinding.datePicker
        mSwipeRefreshLayout = mBinding.swipeRefresh
    }

    override fun onStart() {
        super.onStart()
        initAdapter()
        initToday()
        loadValuteData()


        mSwipeRefreshLayout.setOnRefreshListener {
            loadValuteData()
        }
    }

    private fun loadValuteData() {
        mSwipeRefreshLayout.isRefreshing = true
        parse(valutes_link) { valutesXml ->
            updateDate(viewModel, valutesXml) { updatedData ->
                adapter.refreshUsers(updatedData)
                mSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun initAdapter() {
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        adapter = RecyclerViewAdapter()

        //подписываем адаптер на изменения списка
        viewModel.getListUsers().observe(APP_ACTIVITY, Observer {
            it?.let {
                adapter.refreshUsers(it)
            }
        })

        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter
    }

    private fun initToday() {
        val today = Calendar.getInstance()

        valutes_link =
            "https://www.cbr.ru/scripts/XML_daily_eng.asp?date_req=${getTodayDate(today)}"
        metall_link =
            "https://www.cbr.ru/scripts/xml_metall.asp?date_req1=${getTodayDate(today)}&date_req2=${
                getTodayDate(today)
            }"

        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)
        )
        { view, year, month, day ->
            val mm: String = if ((month + 1) - (month + 1) % 10 == 0) {
                "0${month + 1}"
            } else {
                (month + 1).toString()
            }
            valutes_link = "https://www.cbr.ru/scripts/XML_daily_eng.asp?date_req=$day/$mm/$year"
            metall_link =
                "https://www.cbr.ru/scripts/xml_metall.asp?date_req1=$day/$mm/$year&date_req2=$day/$mm/$year"
            textLink.text = "$day/$mm/$year"
        }
    }

}

