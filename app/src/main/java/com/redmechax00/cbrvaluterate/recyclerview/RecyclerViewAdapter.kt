package com.redmechax00.cbrvaluterate.recyclerview

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.redmechax00.cbrvaluterate.R
import com.redmechax00.cbrvaluterate.databinding.ValuteItemBinding
import com.redmechax00.cbrvaluterate.models.ValuteModel
import com.redmechax00.cbrvaluterate.utilits.ADAPTER_ACTION_UPDATE
import com.redmechax00.cbrvaluterate.utilits.APP_ACTIVITY

class RecyclerViewAdapter(private val onItemClicked: (position: Int) -> Unit) :
    RecyclerView.Adapter<RecyclerViewAdapter.ValuteHolder>() {
    private var valutes: ArrayList<ValuteModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValuteHolder {
        return ValuteHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.valute_item, parent, false),
            onItemClicked
        )
    }

    override fun onBindViewHolder(viewHolder: ValuteHolder, position: Int) {
        viewHolder.bind(valutes[position])
    }

    override fun getItemCount() = valutes.size

    @SuppressLint("NotifyDataSetChanged")
    fun refreshItems(action: Int, items: ArrayList<ValuteModel>) {
        this.valutes = items
        when (action) {
            ADAPTER_ACTION_UPDATE -> notifyDataSetChanged()
        }
    }

    class ValuteHolder(itemView: View, private val onItemClicked: (position: Int) -> Unit) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val valuteIcon = itemView.findViewById<ImageView>(R.id.valute_icon)
        private val valuteNominal = itemView.findViewById<TextView>(R.id.valute_nominal)
        private val valuteCharCode = itemView.findViewById<TextView>(R.id.valute_char_code)
        private val valuteName = itemView.findViewById<TextView>(R.id.valute_name)
        private val valuteValue = itemView.findViewById<TextView>(R.id.valute_value)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val position = absoluteAdapterPosition
            onItemClicked(position)
        }

        fun bind(user: ValuteModel) = with(itemView) {
            valuteIcon.setImageDrawable(user.icon)
            valuteNominal.text = user.nominal
            valuteCharCode.text = user.charCode
            valuteName.text = user.name
            "${user.value} ${APP_ACTIVITY.getString(R.string.valute_sign_rub)}".also {
                valuteValue.text = it
            }
        }
    }
}