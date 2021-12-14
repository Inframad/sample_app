package com.test.a2021_q4_tyukavkin.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.test.a2021_q4_tyukavkin.App
import com.test.a2021_q4_tyukavkin.R
import com.test.a2021_q4_tyukavkin.databinding.ActivityMainBinding
import com.test.a2021_q4_tyukavkin.presentation.viewmodel.MainActivityViewModel
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainActivityViewModel

    private var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]

        super.onCreate(savedInstanceState)
        sharedPref = this.getSharedPreferences("lang", Context.MODE_PRIVATE)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isAuthorized.observe(this, {
            if (it) {
                if (savedInstanceState == null) {
                    val navController = findNavController(R.id.my_nav_host_fragment)
                    val graph = navController.navInflater.inflate(R.navigation.navigation)
                    graph.startDestination = R.id.loans_history_dest
                    navController.graph = graph
                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.enUs -> {
                with(sharedPref?.edit()) {
                    this?.putString("lang", "en")
                    this?.apply()
                }
            }
            R.id.ru -> {
                with(sharedPref?.edit()) {
                    this?.putString("lang", "ru")
                    this?.apply()
                }
            }
        }
        Toast.makeText(
            this,
            getString(R.string.change_language_msg),
            Toast.LENGTH_SHORT
        ).show()
        return true
    }
}