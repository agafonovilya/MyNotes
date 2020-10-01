package ru.geekbrains.mynotes.ui.splash

import android.os.Handler
import org.koin.android.viewmodel.ext.android.viewModel
import ru.geekbrains.mynotes.ui.base.BaseActivity
import ru.geekbrains.mynotes.ui.main.MainActivity
import ru.geekbrains.mynotes.viewmodel.spalsh.SplashViewModel

private const val START_DELAY = 1000L

class SplashActivity : BaseActivity<Boolean?>() {

    override val viewModel: SplashViewModel by viewModel()

    override val layoutRes: Int = ru.geekbrains.mynotes.R.layout.activity_splash

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, START_DELAY)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf{ it }?.let {
            startMainActivity()
        }
    }


    private fun startMainActivity() {
        startActivity(MainActivity.getStartIntent(this))
        finish()
    }
}