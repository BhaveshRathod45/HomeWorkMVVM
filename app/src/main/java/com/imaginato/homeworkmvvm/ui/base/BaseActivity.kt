package com.imaginato.homeworkmvvm.ui.base

import android.app.Activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.koin.core.component.KoinApiExtension

@OptIn(KoinApiExtension::class)
open class BaseActivity : AppCompatActivity() {

    private var mViewModel: BaseViewModel? = null
    private var mProgress: ProgressDialog? = null
    private var mActivity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivity = this
        mProgress = ProgressDialog(this)
        mProgress?.setCancelable(false)
    }


    protected fun initViewModel(viewModel: BaseViewModel) {
        this.mViewModel = viewModel
        initObserve()
    }

    private fun initObserve() {
        mViewModel?.progress?.observe(this) {
            if (mProgress != null) {
                if (it) {
                    mProgress?.show()
                } else {
                    mProgress?.dismiss()
                }
            }

        }
        mViewModel?.errorMessage?.observe(this) {
            if (it.isNotEmpty())
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        mActivity = null
        mViewModel = null
        mProgress?.dismiss()
        mProgress = null
        super.onDestroy()
    }
}