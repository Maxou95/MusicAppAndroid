package com.ald47.project.musicapp

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ald47.project.musicapp.databinding.ActivityHomeBinding

class Accueil : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)

        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.apply {
                when(destination.id){
                    R.id.rechercherFragment-> {
                        fragTitleTV.visibility = View.VISIBLE
                        fragTitleTV.text = resources.getString(R.string.recherche)
                    }
                    R.id.favorisFragment-> {
                        fragTitleTV.visibility = View.VISIBLE
                        fragTitleTV.text = resources.getString(R.string.favoris)
                    }
                    R.id.classementsFragment -> {
                        fragTitleTV.visibility = View.VISIBLE
                        fragTitleTV.text = resources.getString(R.string.classements)
                    }
                    else->fragTitleTV.visibility = View.GONE
                }
            }
        }
    }
}