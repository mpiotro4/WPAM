package com.example.aswitch.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import com.example.aswitch.Recipe
import com.example.aswitch.adapters.IngredientAdapter
import com.google.android.material.chip.Chip
import com.release.gfg1.DBHelper
import kotlinx.android.synthetic.main.activity_second.*
import kotlinx.android.synthetic.main.activity_second.cgKeyWords
import kotlinx.android.synthetic.main.activity_second.etCost
import kotlinx.android.synthetic.main.activity_second.etRecipeName
import kotlinx.android.synthetic.main.activity_second.etTime
import kotlinx.android.synthetic.main.activity_second.rvIngredients
import java.io.Serializable

class SecondActivity : AppCompatActivity() {
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var keyWords: ArrayList<String>
    private lateinit var ingredients: MutableList<Ingredient>
    private lateinit var recipe: Recipe
    private var ifUpdate: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        keyWords = arrayListOf()
        ingredients = mutableListOf()
        intent.extras?.get("extra_recipe")?.let {
            recipe = it as Recipe
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ifUpdate = intent.getBooleanExtra("extra_if_update", false)
        val dbHelper = DBHelper(this, null)

        populateUIElements()

        ingredientAdapter = IngredientAdapter(ingredients)
        rvIngredients.adapter = ingredientAdapter
        rvIngredients.layoutManager = LinearLayoutManager(this)

        btnAddPhoto.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else{
                    chooseImageGallery();
                }
            }else{
                chooseImageGallery();
            }
        }

        btnMakePhoto.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivity(gallery)
        }

        btnBack.setOnClickListener {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
            }
        }

        etAddIngredients.setOnClickListener {
            Intent(this, AddIngredientsActivity::class.java).also {
                putExtras(it)
                startActivity(it)
            }
        }

        etAddKeyWords.setOnClickListener {
            Intent(this, AddKeyWordsActivity::class.java).also {
                putExtras(it)
                startActivity(it)
            }
        }

        btnAdd.setOnClickListener {
            if(etRecipeName.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Nazwa przepisu nie może być pusta",Toast.LENGTH_SHORT).show()
            } else {
                if(ifUpdate) {
//                    Todo update recipe logic
                    Toast.makeText(applicationContext,"Przepis zauktualizowany pomyślnie",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    dbHelper.addRecipe(
                        etRecipeName.text.toString(),
                        etCost.text.toString(),
                        etTime.text.toString(),
                        keyWords,
                        ingredients
                    )
                    Toast.makeText(applicationContext,"Przepis dodany pomyślnie",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun putExtras(intent: Intent) {
        intent.putExtra("extra_ingredients", ArrayList(ingredients))
        intent.putExtra("extra_keyWords", keyWords)
        if (this::recipe.isInitialized) {
            recipe.time = etTime.text.toString()
            recipe.title = etRecipeName.text.toString()
            recipe.cost = etCost.text.toString()
        } else {
            recipe = Recipe(
                "",
                etRecipeName.text.toString(),
                etCost.text.toString(),
                etTime.text.toString(),
                keyWords)
        }
        intent.putExtra("extra_recipe", recipe as Serializable)
    }

    private fun addChip(txt: String) {
        val chip = Chip(this)
        chip.apply {
            text = txt
            chipIcon = ContextCompat.getDrawable(
                this@SecondActivity,
                R.drawable.ic_launcher_background
            )
            isChipIconVisible = false
            isCloseIconVisible = true
            isClickable = true
            isCheckable = false
            cgKeyWords.addView(chip as View)
            setOnCloseIconClickListener {
                cgKeyWords.removeView(chip as View)
                removeChip(txt)
            }
        }
    }

    private fun populateUIElements() {
        intent.getParcelableArrayListExtra<Ingredient>("extra_ingredients")?.let {
            ingredients =
                intent.getParcelableArrayListExtra<Ingredient>("extra_ingredients") as ArrayList<Ingredient>
        }
        intent.getStringArrayListExtra("extra_key_words")?.let {
            keyWords = it
            keyWords.forEach { addChip(it) }
        }
        intent.extras?.get("extra_recipe")?.let {
            recipe = it as Recipe
            etRecipeName.setText(recipe.title)
            etCost.setText(recipe.cost)
            etTime.setText(recipe.time)
        }
    }

    companion object {
        private val IMAGE_CHOOSE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    chooseImageGallery()
                }else{
                    Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imageView.setImageURI(data?.data)
    }

    private fun removeChip(txt: String) {
        keyWords.remove(txt)
    }
}