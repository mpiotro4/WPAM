package com.example.aswitch.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.aswitch.R

class ReadRecipeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_recipe)
    }

    override fun onCreateOptionsMenu(menu :Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.read_recipe_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("dupa", "dupa")
        return super.onOptionsItemSelected(item)
    }
}