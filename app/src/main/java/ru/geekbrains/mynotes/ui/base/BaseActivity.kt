package ru.geekbrains.mynotes.ui.base

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import ru.geekbrains.mynotes.R
import ru.geekbrains.mynotes.viewmodel.base.BaseViewModel
import ru.geekbrains.mynotes.viewmodel.base.BaseViewState

abstract class BaseActivity<T,S : BaseViewState<T>> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T,S>
    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        viewModel.getViewState().observe(this, object : Observer<S>{
            override fun onChanged(t: S?) {
                if (t == null) return
                if (t.data != null) renderData(t.data!!)
                if (t.error != null) renderError(t.error)
            }
        })
    }

    protected fun renderError(error: Throwable) {
        if(error.message != null) showError(error.message!!)
    }

    protected fun showError(error: String) {
        val snackbar = Snackbar.make(mainActivity_recycler, error, Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(R.string.ok_bth_title, View.OnClickListener { snackbar.dismiss() })
        snackbar.show()
    }

    abstract fun renderData(data: T)
}