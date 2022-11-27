package com.release.gfg1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = (""" 
            CREATE TABLE $TABLE_NAME (
                $ID_COL INTEGER PRIMARY KEY,
                $TITLE_COL TEXT,
                UNIQUE($TITLE_COL))
            """)
        db.execSQL(query)
        seed(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addIngredient(title : String){
        val values = ContentValues()
        values.put(TITLE_COL, title)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getTitle(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    companion object{
        private val DATABASE_NAME = "cookbook"
        private val DATABASE_VERSION = 6
        val TABLE_NAME = "ingredients"
        val ID_COL = "ingredient_id"
        val TITLE_COL = "title"
    }

    private fun seed(db: SQLiteDatabase) {
        db.execSQL("insert into $TABLE_NAME ($TITLE_COL) values ('Jajko')")
        db.execSQL("insert into $TABLE_NAME ($TITLE_COL) values ('Mleko')")
        db.execSQL("insert into $TABLE_NAME ($TITLE_COL) values ('Ser')")
        db.execSQL("insert into $TABLE_NAME ($TITLE_COL) values ('Szynka')")
        db.execSQL("insert into $TABLE_NAME ($TITLE_COL) values ('Mąka')")
        db.execSQL("insert into $TABLE_NAME ($TITLE_COL) values ('Woda')")
        db.execSQL("insert into $TABLE_NAME ($TITLE_COL) values ('Sól')")
        db.execSQL("insert into $TABLE_NAME ($TITLE_COL) values ('Pieprz')")
        db.execSQL("insert into $TABLE_NAME ($TITLE_COL) values ('Cebula')")
        db.execSQL("insert into $TABLE_NAME ($TITLE_COL) values ('Czosnek')")
    }
}