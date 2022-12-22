package com.imaginato.homeworkmvvm.ui.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.imaginato.homeworkmvvm.R

class ProgressDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_dialog)
    }

}