package com.release.gfg1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.aswitch.Ingredient

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(""" 
            CREATE TABLE $INGREDIENTS_TABLE_NAME (
                $INGREDIENTS_ID_COL INTEGER PRIMARY KEY,
                $INGREDIENTS_TITLE_COL TEXT,
                UNIQUE($INGREDIENTS_TITLE_COL))
            """)
        db.execSQL(""" 
            CREATE TABLE $KEYWORDS_TABLE_NAME (
                $KEYWORDS_ID_COL INTEGER PRIMARY KEY,
                $KEYWORDS_KEYWORD_COL TEXT,
                UNIQUE($KEYWORDS_KEYWORD_COL))
            """)
        db.execSQL(""" 
            CREATE TABLE $RECIPES_TABLE_NAME (
                $RECIPES_ID_COL INTEGER PRIMARY KEY,
                $RECIPES_TITLE_COL TEXT NOT NULL,
                $RECIPES_TIME_COL INTEGER,
                $RECIPES_COST_COL REAL
                )
            """)
        db.execSQL(""" 
            CREATE TABLE $RECIPE_INGREDIENTS_TABLE_NAME (
                $RECIPES_ID_COL INTEGER,
                $INGREDIENTS_ID_COL INTEGER,
                $RECIPE_INGREDIENTS_QUANTITY_COL TEXT,
                FOREIGN KEY($RECIPES_ID_COL) REFERENCES $RECIPES_TABLE_NAME($RECIPES_ID_COL),
                FOREIGN KEY($INGREDIENTS_ID_COL) REFERENCES $INGREDIENTS_TABLE_NAME($INGREDIENTS_ID_COL),
                PRIMARY KEY ($RECIPES_ID_COL, $INGREDIENTS_ID_COL)    
                )
            """)
        db.execSQL(""" 
            CREATE TABLE $RECIPE_KEYWORDS_TABLE_NAME (
                $RECIPES_ID_COL INTEGER,
                $KEYWORDS_ID_COL INTEGER,
                FOREIGN KEY($RECIPES_ID_COL) REFERENCES $RECIPES_TABLE_NAME($RECIPES_ID_COL),
                FOREIGN KEY($KEYWORDS_ID_COL) REFERENCES $KEYWORDS_TABLE_NAME($KEYWORDS_ID_COL),
                PRIMARY KEY ($RECIPES_ID_COL, $KEYWORDS_ID_COL)    
                )
            """)
        seed(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $INGREDIENTS_TABLE_NAME")
        onCreate(db)
    }

    fun addRecipe(title: String?, time: String, cost: String, keyWords: ArrayList<String>, ingredients: MutableList<Ingredient>){
        val values = ContentValues()
        values.put(RECIPES_TITLE_COL, title)
        values.put(RECIPES_COST_COL, cost)
        values.put(RECIPES_TIME_COL, time)
        val db = this.writableDatabase
        val recipeId = db.insert(RECIPES_TABLE_NAME, null, values)
        keyWords.forEach { getKeywordId(it)?.let { it1 -> addRecipeKeyword(recipeId.toString(), it1) } }
        ingredients.forEach {  getIngredientId(it.title)?.let { it1 -> addRecipeIngredient(recipeId.toString(), it1, it.quantity) } }
        db.close()
    }

    fun addIngredient(title : String){
        val values = ContentValues()
        values.put(INGREDIENTS_TITLE_COL, title)
        val db = this.writableDatabase
        db.insert(INGREDIENTS_TABLE_NAME, null, values)
        db.close()
    }

    private fun addRecipeKeyword(recipeId: String, keywordId: String) {
        val values = ContentValues()
        values.put(RECIPES_ID_COL, recipeId)
        values.put(KEYWORDS_ID_COL, keywordId)
        val db = this.writableDatabase
        db.insert(RECIPE_KEYWORDS_TABLE_NAME, null, values)
        db.close()
    }

    private fun addRecipeIngredient(recipeId: String, ingredientId: String, quantity: String) {
        val values = ContentValues()
        values.put(RECIPES_ID_COL, recipeId)
        values.put(INGREDIENTS_ID_COL, ingredientId)
        values.put(RECIPE_INGREDIENTS_QUANTITY_COL, quantity)
        val db = this.writableDatabase
        db.insert(RECIPE_INGREDIENTS_TABLE_NAME, null, values)
        db.close()
    }

    private fun getIngredientId(title: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("""SELECT $INGREDIENTS_ID_COL FROM $INGREDIENTS_TABLE_NAME WHERE $INGREDIENTS_TITLE_COL = "$title" """, null)
        cursor.moveToFirst()
        return cursor.getString(cursor.getColumnIndexOrThrow(INGREDIENTS_ID_COL))
    }

    private fun getKeywordId(keyword: String): String? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("""SELECT $KEYWORDS_ID_COL FROM $KEYWORDS_TABLE_NAME WHERE $KEYWORDS_KEYWORD_COL = "$keyword" """, null)
        cursor.moveToFirst()
        return cursor.getString(cursor.getColumnIndexOrThrow(KEYWORDS_ID_COL))
    }

    fun getTitle(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $INGREDIENTS_TABLE_NAME", null)
    }

    fun getRecipes(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $RECIPES_TABLE_NAME", null)
    }

    fun getKeyword(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $KEYWORDS_TABLE_NAME", null)
    }

    companion object{
        private const val DATABASE_NAME = "cookbook"
        private const val DATABASE_VERSION = 3

        const val INGREDIENTS_TABLE_NAME = "ingredients"
        const val INGREDIENTS_ID_COL = "ingredient_id"
        const val INGREDIENTS_TITLE_COL = "title"

        const val KEYWORDS_TABLE_NAME = "keywords"
        const val KEYWORDS_ID_COL = "keyword_id"
        const val KEYWORDS_KEYWORD_COL = "keyword"

        const val RECIPES_TABLE_NAME = "recipes"
        const val RECIPES_ID_COL = "recipe_id"
        const val RECIPES_TITLE_COL = "title"
        const val RECIPES_TIME_COL = "time"
        const val RECIPES_COST_COL = "cost"

        const val RECIPE_INGREDIENTS_TABLE_NAME = "recipe_ingredients"
        const val RECIPE_INGREDIENTS_QUANTITY_COL = "quantity"

        const val RECIPE_KEYWORDS_TABLE_NAME = "recipe_keywords"
    }

    private fun seed(db: SQLiteDatabase) {
        db.execSQL("insert into $INGREDIENTS_TABLE_NAME ($INGREDIENTS_TITLE_COL) values ('Jajko')")
        db.execSQL("insert into $INGREDIENTS_TABLE_NAME ($INGREDIENTS_TITLE_COL) values ('Mleko')")
        db.execSQL("insert into $INGREDIENTS_TABLE_NAME ($INGREDIENTS_TITLE_COL) values ('Ser')")
        db.execSQL("insert into $INGREDIENTS_TABLE_NAME ($INGREDIENTS_TITLE_COL) values ('Szynka')")
        db.execSQL("insert into $INGREDIENTS_TABLE_NAME ($INGREDIENTS_TITLE_COL) values ('Mąka')")
        db.execSQL("insert into $INGREDIENTS_TABLE_NAME ($INGREDIENTS_TITLE_COL) values ('Woda')")
        db.execSQL("insert into $INGREDIENTS_TABLE_NAME ($INGREDIENTS_TITLE_COL) values ('Sól')")
        db.execSQL("insert into $INGREDIENTS_TABLE_NAME ($INGREDIENTS_TITLE_COL) values ('Pieprz')")
        db.execSQL("insert into $INGREDIENTS_TABLE_NAME ($INGREDIENTS_TITLE_COL) values ('Cebula')")
        db.execSQL("insert into $INGREDIENTS_TABLE_NAME ($INGREDIENTS_TITLE_COL) values ('Czosnek')")

        db.execSQL("insert into $KEYWORDS_TABLE_NAME ($KEYWORDS_KEYWORD_COL) values ('Wegański')")
        db.execSQL("insert into $KEYWORDS_TABLE_NAME ($KEYWORDS_KEYWORD_COL) values ('Wegetariański')")
        db.execSQL("insert into $KEYWORDS_TABLE_NAME ($KEYWORDS_KEYWORD_COL) values ('Tani')")
        db.execSQL("insert into $KEYWORDS_TABLE_NAME ($KEYWORDS_KEYWORD_COL) values ('Drogi')")
        db.execSQL("insert into $KEYWORDS_TABLE_NAME ($KEYWORDS_KEYWORD_COL) values ('nie dobry')")
        db.execSQL("insert into $KEYWORDS_TABLE_NAME ($KEYWORDS_KEYWORD_COL) values ('Bez gluetnu')")
        db.execSQL("insert into $KEYWORDS_TABLE_NAME ($KEYWORDS_KEYWORD_COL) values ('Fit')")

        db.execSQL("insert into $RECIPES_TABLE_NAME ($RECIPES_ID_COL, $RECIPES_TITLE_COL, $RECIPES_TIME_COL, $RECIPES_COST_COL) values (1 ,'Chili con carne', '25', '60')")
        db.execSQL("insert into $RECIPE_INGREDIENTS_TABLE_NAME ($RECIPES_ID_COL, $INGREDIENTS_ID_COL, $RECIPE_INGREDIENTS_QUANTITY_COL) values (1, 1, 'sztuka')")
        db.execSQL("insert into $RECIPE_INGREDIENTS_TABLE_NAME ($RECIPES_ID_COL, $INGREDIENTS_ID_COL, $RECIPE_INGREDIENTS_QUANTITY_COL) values (1, 2, '100ml')")
        db.execSQL("insert into $RECIPE_INGREDIENTS_TABLE_NAME ($RECIPES_ID_COL, $INGREDIENTS_ID_COL, $RECIPE_INGREDIENTS_QUANTITY_COL) values (1, 3, '250g')")

        db.execSQL("insert into $RECIPE_KEYWORDS_TABLE_NAME ($RECIPES_ID_COL, $KEYWORDS_ID_COL) values (1, 1)")
        db.execSQL("insert into $RECIPE_KEYWORDS_TABLE_NAME ($RECIPES_ID_COL, $KEYWORDS_ID_COL) values (1, 2)")
        db.execSQL("insert into $RECIPE_KEYWORDS_TABLE_NAME ($RECIPES_ID_COL, $KEYWORDS_ID_COL) values (1, 3)")
    }
}