package com.test.a2021_q4_tyukavkin.ui

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as App).appComponent.inject(this)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[MainActivityViewModel::class.java]

        super.onCreate(savedInstanceState)
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

}