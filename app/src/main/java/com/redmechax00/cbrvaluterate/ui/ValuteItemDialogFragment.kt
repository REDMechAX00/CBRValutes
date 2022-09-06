package com.redmechax00.cbrvaluterate.ui

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ValuteItemDialogFragment constructor(
    _title: String,
    _icon: Drawable?,
    private val onItemClicked: (position: Int) -> Unit
) : DialogFragment() {

    private val actions = arrayOf("Выбрать основной валютой")
    private var title: String
    private var icon: Drawable?

    init {
        title = _title
        icon = _icon
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(title)
                .setIcon(icon)
                .setItems(actions)
                { _, position ->
                    onItemClicked(position)
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}