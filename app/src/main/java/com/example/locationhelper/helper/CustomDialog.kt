package com.example.locationhelper.helper

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.example.locationhelper.databinding.PopLocationLayoutBinding

object CustomDialog {
    var dialog: Dialog?=null

    fun showDialogToEnableLocation(
        baseActivity: Context,
        enableLocationButtonClickListener: (Boolean) -> Unit,
        ) {
        val dialogBuilder: AlertDialog.Builder =
            AlertDialog.Builder(baseActivity)

        val bind: PopLocationLayoutBinding =
            PopLocationLayoutBinding.inflate(LayoutInflater.from(baseActivity))

        if (bind.root != null) {
            (bind.root as ViewGroup).removeView(bind.root)
        }
        dialogBuilder
            .setView(bind.root)
            .setCancelable(false)
        dialog = dialogBuilder.create()
        dialog?.window?.let {
            it.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )

            val wlp: WindowManager.LayoutParams = it.attributes
            wlp.gravity = Gravity.TOP

            wlp.y = 300 //y position

            it.attributes = wlp
        }
        dialog?.show()


        bind.logoutBtnYes.setOnClickListener {
            // no button id = -1
            dialog?.dismiss()
            enableLocationButtonClickListener(true)
        }
    }

}