package com.hoamz.auth

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable

open class BaseActivity : AppCompatActivity() {

    open fun onToast(msg : String){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show()
    }

    open fun showDialogNotify(msg : String){
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        val inflate = layoutInflater
        val dialogView = inflate.inflate(R.layout.dialog_toast,null)
        builder.setView(dialogView)
        builder.setCancelable(true)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //mac dinh o giua man hinh
        dialog.setCanceledOnTouchOutside(false)
        //show msg
        val showMsg : TextView = dialogView.findViewById(R.id.tvShowToast)
        val btnClose : Button = dialogView.findViewById(R.id.btnClose)
        showMsg.text = msg
        //onClick
        btnClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}