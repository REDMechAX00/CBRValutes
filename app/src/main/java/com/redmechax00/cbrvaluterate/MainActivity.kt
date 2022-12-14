package com.redmechax00.cbrvaluterate

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.redmechax00.cbrvaluterate.database.ValuteDao
import com.redmechax00.cbrvaluterate.database.ValuteDatabase
import com.redmechax00.cbrvaluterate.databinding.ActivityMainBinding
import com.redmechax00.cbrvaluterate.models.ValuteModel
import com.redmechax00.cbrvaluterate.recyclerview.RecyclerViewAdapter
import com.redmechax00.cbrvaluterate.recyclerview.ValuteViewModel
import com.redmechax00.cbrvaluterate.utilits.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var valutes_link: String
    private lateinit var metal_link: String

    //Эта переменная только ради анимации перемещения итема адаптера вместо анимации refresh
    private var adapter_action: Int = ADAPTER_ACTION_UPDATE

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: ValuteViewModel
    private lateinit var adapter: RecyclerViewAdapter

    private lateinit var mainValuteIcon: ImageView
    private lateinit var mainValuteCharCode: TextView
    private lateinit var mainValuteName: TextView
    private lateinit var mainValuteDate: TextView
    private lateinit var mainValuteNominal: TextView

    private lateinit var db: ValuteDatabase
    private lateinit var valuteDao: ValuteDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this

        recyclerView = mBinding.recyclerView

        mainValuteIcon = mBinding.mainValuteIcon
        mainValuteCharCode = mBinding.mainValuteCharCode
        mainValuteName = mBinding.mainValuteName
        mainValuteDate = mBinding.mainValuteDate
        mainValuteNominal = mBinding.mainValuteNominal

        initDatabase()
        initAdapter()
        initMainValute()

        mSwipeRefreshLayout = mBinding.swipeRefresh
        mSwipeRefreshLayout.setOnRefreshListener {
            updateValuteData()
        }

    }

    private fun initMainValute() {
        mainValuteIcon.setImageResource(R.drawable.ic_russian_federation)
        mainValuteCharCode.text = getString(R.string.valute_char_code_rub)
        mainValuteName.text = getString(R.string.valute_name_rub)
        mainValuteNominal.text = "1"
    }

    override fun onStart() {
        super.onStart()
        initToday()
        updateValuteData()
    }

    private fun updateValuteData() {
        mSwipeRefreshLayout.isRefreshing = parse(valutes_link) { listOfXmlValutes ->
            updateData(viewModel, listOfXmlValutes) { updatedData ->
                viewModel.getListValutes().value = updatedData

                "${getString(R.string.app_name)} (${updatedData[0].date})".also {
                    APP_ACTIVITY.title = it
                }

                mSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun initDatabase() {
        db = Room.databaseBuilder(
            applicationContext,
            ValuteDatabase::class.java, "database-valutes"
        ).build()

        valuteDao = db.getValuteDAO()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun saveDataToDatabase(newData: ArrayList<ValuteModel>) {
        GlobalScope.launch(Dispatchers.IO) {
            newData.forEachIndexed { index, valute ->
                valuteDao.add(valute)
            }
        }
    }

    private fun initAdapter() {
        viewModel = ViewModelProvider(this)[ValuteViewModel::class.java]
        adapter = RecyclerViewAdapter { position -> /*onListItemClick(position)*/ }

        //подписываем адаптер на изменения списка
        viewModel.getListValutes().observe(APP_ACTIVITY) {
            it?.let {
                adapter.refreshItems(adapter_action, it)
                saveDataToDatabase(it)
                adapter_action = ADAPTER_ACTION_UPDATE
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun initToday() {
        val today = Calendar.getInstance()
        valutes_link =
            "https://www.cbr.ru/scripts/XML_daily_eng.asp?date_req=${getTodayDate(today)}"
        metal_link =
            "https://www.cbr.ru/scripts/xml_metall.asp?date_req1=${getTodayDate(today)}&date_req2=${
                getTodayDate(today)
            }"
    }

    //Переместить итем с позиции fromPosition на позицию toPosition
    /*private fun moveItems(fromPosition: Int, toPosition: Int) {
        viewModel.getListValutes().value?.let { listValutes ->
            val item: Valute = listValutes[fromPosition]
            listValutes.removeAt(fromPosition)
            listValutes.add(toPosition, item)

            listValutes.forEachIndexed { index, valute ->
                valute.uid = index
            }

            adapter_action = ADAPTER_ACTION_MOVE
            viewModel.getListValutes().value = listValutes
            adapter.notifyItemMoved(fromPosition, toPosition)
        }
    }*/

    //Реализация диалогового окна с выбором действия при нажатии на итем
    /*private fun onListItemClick(position: Int) {
        viewModel.getListValutes().value?.let { arrayListValute ->
            val icon = arrayListValute[position].icon
            val title = arrayListValute[position].charCode

            val mValuteItemDialogFragment = ValuteItemDialogFragment(title, icon) { actions ->
                when (actions) {
                    DIALOG_ACTION_CHOOSE_MAIN_VALUTE -> {

                    }
                }
            }
            val manager = supportFragmentManager
            mValuteItemDialogFragment.show(manager, "mValuteItemDialogFragment")
        }
    }*/

}

