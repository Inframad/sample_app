package com.test.a2021_q4_tyukavkin.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.ActivityMainBinding
import com.test.a2021_q4_tyukavkin.presentation.LocaleChanger
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.MainActivityViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainActivityViewModel

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]

        super.onCreate(savedInstanceState)
        sharedPref = this.getSharedPreferences("lang", Context.MODE_PRIVATE)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isAuthorized.observe(this, { isAuthorized ->
            if (!isAuthorized) {
                Navigation.findNavController(binding.myNavHostFragment).apply {
                    popBackStack()
                    navigate(R.id.registration_dest)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    @SuppressLint("RestrictedApi")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.enUs -> {
                with(sharedPref?.edit()) {
                    this?.putString("lang", "en")
                    this?.apply()
                }
                recreate()
            }
            R.id.ru -> {
                with(sharedPref?.edit()) {
                    this?.putString("lang", "ru")
                    this?.apply()
                }
                recreate()
            }
            R.id.logout -> {
                viewModel.logout()
                Navigation.findNavController(binding.myNavHostFragment).apply {
                    repeat(this.backStack.size) { popBackStack() }
                    navigate(R.id.to_registration)
                }
            }
        }
        return true
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleChanger.wrapContext(base))
    }

}