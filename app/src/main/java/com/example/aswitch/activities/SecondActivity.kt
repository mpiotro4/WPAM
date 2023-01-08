package com.example.aswitch.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aswitch.Ingredient
import com.example.aswitch.R
import com.example.aswitch.Recipe
import com.example.aswitch.adapters.IngredientAdapter
import com.google.android.material.chip.Chip
import com.release.gfg1.DBHelper
import kotlinx.android.synthetic.main.activity_second.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.Serializable


class SecondActivity : AppCompatActivity() {
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var keyWords: ArrayList<String>
    private lateinit var ingredients: MutableList<Ingredient>
    private lateinit var recipe: Recipe
    private var ifUpdate: Boolean = false
    private lateinit var img: ByteArray

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

        btnMakePhoto.setOnClickListener{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, 0)
        }

        btnAddPhoto.setOnClickListener{
            openGalleryForImage()
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
                    dbHelper.addRecipeWithImg(
                        etRecipeName.text.toString(),
                        etCost.text.toString(),
                        etTime.text.toString(),
                        keyWords,
                        ingredients,
                        img
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
        if(this::img.isInitialized) {
            intent.putExtra("extra_img", img)
        }
        intent.putExtra("extra_recipe", recipe as Serializable)
        intent.putExtra("extra_if_update", ifUpdate)
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
        intent.getByteArrayExtra("extra_img")?.let {
            img = it
            val bmp = img.let { it1 -> BitmapFactory.decodeByteArray(img, 0, it1.lastIndex) }
            imageView.setImageBitmap(bmp)
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100){
//            imageView.setImageURI(data?.data)
            val iStream: InputStream? = data?.data?.let { contentResolver.openInputStream(it) }
            val inputData: ByteArray? = iStream?.let { getBytes(it) }
            if (inputData != null) {
                img = inputData
            }

            val bmp = img.let { BitmapFactory.decodeByteArray(img, 0, it.lastIndex) }
            imageView.setImageBitmap(bmp)
        }
        if (resultCode == Activity.RESULT_OK && requestCode == 0 && data != null){
            val bitmap = data.extras?.get("data") as Bitmap
            imageView.setImageBitmap(bitmap)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            img = stream.toByteArray()
        }
    }

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    private fun removeChip(txt: String) {
        keyWords.remove(txt)
    }
}