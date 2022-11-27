package com.release.gfg1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
        seed(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $INGREDIENTS_TABLE_NAME")
        onCreate(db)
    }

    fun addIngredient(title : String){
        val values = ContentValues()
        values.put(INGREDIENTS_TITLE_COL, title)
        val db = this.writableDatabase
        db.insert(INGREDIENTS_TABLE_NAME, null, values)
        db.close()
    }

    fun getTitle(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $INGREDIENTS_TABLE_NAME", null)
    }

    fun getKeyword(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $KEYWORDS_TABLE_NAME", null)
    }

    companion object{
        private const val DATABASE_NAME = "cookbook"
        private const val DATABASE_VERSION = 7

        const val INGREDIENTS_TABLE_NAME = "ingredients"
        const val INGREDIENTS_ID_COL = "ingredient_id"
        const val INGREDIENTS_TITLE_COL = "title"

        const val KEYWORDS_TABLE_NAME = "keywords"
        const val KEYWORDS_ID_COL = "keyword_id"
        const val KEYWORDS_KEYWORD_COL = "keyword"
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
    }
}