package com.example.receitas.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.receitas.R
import com.example.receitas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Define os destinos de topo da navegação (não terão o botão "Up")
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.categoryListFragment, R.id.randomRecipeFragment, R.id.favoritesFragment)
        )

        // Liga a Toolbar ao NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Liga a BottomNavigationView ao NavController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    // Permite que o botão "Up" (seta para trás) na Toolbar funcione
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
