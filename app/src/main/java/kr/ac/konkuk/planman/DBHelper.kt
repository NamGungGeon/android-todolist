package kr.ac.konkuk.planman

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){
    companion object {
        val DB_NAME = "planman.db"
        val DB_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createData =
            "CREATE TABLE IF NOT EXISTS data (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT," +
                    "content TEXT," +
                    "type TEXT ," +
                    "att_webSite text," +
                    "att_phoneNumber text," +
                    "att_location text," +
                    "not_notifyDateTime text," +
                    "not_notifyRadius text);"

        val createCategory =
            "CREATE TABLE IF NOT EXISTS category (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "type TEXT," +
                    "textSize TEXT," +
                    "textColor TEXT," +
                    "textStyle TEXT);"

        db!!.execSQL(createData)
        db!!.execSQL(createCategory)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS data;")
        db!!.execSQL("DROP TABLE IF EXISTS category;")
        onCreate(db)
    }
}