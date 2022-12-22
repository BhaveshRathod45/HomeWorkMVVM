package com.imaginato.homeworkmvvm.ui.login

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.imaginato.homeworkmvvm.databinding.ActivityLoginBinding
import com.imaginato.homeworkmvvm.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension

@KoinApiExtension
class LoginActivity : BaseActivity() {

    private val viewModel by viewModel<LoginViewModel>()
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel(viewModel)
        initListener()
        initObserve()
    }

    private fun initListener() {
        binding.btnLogin.setOnClickListener {
            viewModel.doLogin(
                binding.edtUserName.text.toString(),
                binding.edtPassword.text.toString()
            )
        }
    }

    private fun initObserve() {
        viewModel.userLiveData.observe(this) {
            Toast.makeText(this, "Login with User : "+it.name, Toast.LENGTH_SHORT).show()
        }
    }
}