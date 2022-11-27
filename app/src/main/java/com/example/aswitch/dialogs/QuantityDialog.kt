package com.example.aswitch.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.aswitch.R

class QuantityDialog (
    private val title: String
) : AppCompatDialogFragment()  {
    private var listener: ExampleDialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view: View = inflater.inflate(R.layout.layout_quantity_dialog, null)

        val etQuantity = view.findViewById<EditText>(R.id.etQuantity)
        return AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle(title)
            .setNegativeButton("Anuluj") {_, _, ->}
            .setPositiveButton("Ok") {_, _, ->
                listener!!.changeActivity(etQuantity.text.toString(), title)
                listener!!.addIngredientToDB(title)
            }
            .create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as ExampleDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context.toString() +
                        "must implement ExampleDialogListener"
            )
        }
    }

    interface ExampleDialogListener {
        fun applyTexts(username: String?, password: String?)
        fun changeActivity(quantity: String, title: String)
        fun addIngredientToDB(title: String)
    }
}