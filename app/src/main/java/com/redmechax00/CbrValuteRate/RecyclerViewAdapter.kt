package com.redmechax00.CbrValuteRate

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.valute_item.view.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter(private val onItemClicked: (position: Int) -> Unit) :
    RecyclerView.Adapter<RecyclerViewAdapter.ValuteHolder>() {
    private var valutes: ArrayList<Valute> = ArrayList()

    //создает ViewHolder и инициализирует views для списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValuteHolder {
        return ValuteHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.valute_item, parent, false),
            onItemClicked
        )
    }

    //связывает views с содержимым
    override fun onBindViewHolder(viewHolder: ValuteHolder, position: Int) {
        viewHolder.bind(valutes[position])
    }

    override fun getItemCount() = valutes.size

    //передаем данные и оповещаем адаптер о необходимости обновления списка
    @SuppressLint("NotifyDataSetChanged")
    fun refreshItems(action: Int, items: ArrayList<Valute>) {
        this.valutes = items
        when(action){
            ADAPTER_ACTION_UPDATE -> notifyDataSetChanged()
        }
    }


    //внутренний класс ViewHolder описывает элементы представления списка и привязку их к RecyclerView
    class ValuteHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = absoluteAdapterPosition
            onItemClicked(position)
        }

        fun bind(user: Valute) = with(itemView) {
            valute_icon.setImageDrawable(user.icon)
            valute_nominal.text = user.nominal
            valute_char_code.text = user.charCode
            valute_name.text = user.name
            "${user.value} ${APP_ACTIVITY.getString(R.string.valute_sign_rub)}".also { valute_value.text = it }
        }
    }
}