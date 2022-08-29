package com.redmechax00.parsexml
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import kotlinx.android.synthetic.main.valute_item.view.*

class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.UserHolder>() {

    private var valutes: List<Valute> = ArrayList()

    //создает ViewHolder и инициализирует views для списка
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.valute_item, parent, false)
        )
    }

    //связывает views с содержимым
    override fun onBindViewHolder(viewHolder: UserHolder, position: Int) {
        viewHolder.bind(valutes[position])
    }

    override fun getItemCount() = valutes.size

    //передаем данные и оповещаем адаптер о необходимости обновления списка
    @SuppressLint("NotifyDataSetChanged")
    fun refreshUsers(users: List<Valute>) {
        this.valutes = users
        notifyDataSetChanged()
    }


    //внутренний класс ViewHolder описывает элементы представления списка и привязку их к RecyclerView
    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: Valute) = with(itemView) {
            valute_icon.setImageDrawable(user.icon)
            valute_char_code.text = user.charCode
            valute_name.text = user.name
            valute_value.text = user.value
            valute_date.text = user.date
        }
    }
}