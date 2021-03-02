package com.modern.android.formssample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.modern.android.forms.FormRendererFragment
import com.modern.android.forms.SessionAwareActivity
import com.modern.android.forms.entity.FormContext
import com.modern.android.formssample.common.TransactionBuilder
import dagger.android.support.DaggerAppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDate

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SessionAwareActivity {

    private val formContext = FormContext(god = "GODNAME", date = LocalDate.now())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TransactionBuilder(FormRendererFragment::class.java)
            .withArgs(FormRendererFragment.createArguments(formContext))
            .commit(this, R.id.container)
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.filterIsInstance<FormRendererFragment>()
            .firstOrNull()?.onBackPressed() ?: super.onBackPressed()
    }

    override fun sessionExpired(error: Throwable) {
        Log.e("TAG","message: " + error.message)
    }
}