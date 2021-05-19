package com.meditab.task.utilities

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.meditab.task.R
import kotlinx.android.synthetic.main.custom_progress_dialog_layout.view.*

class CustomProgressDialog : DialogFragment() {

    companion object {
        fun customProgressDialog(context: Context, message : String = "Loading..."): Dialog {
            val dialog = Dialog(context)
            val inflate = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog_layout, null)
            dialog.setContentView(inflate)
            inflate.pb_text.text = message
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            return dialog
        }
    }
}