package kr.ac.konkuk.planman

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class DB(val context: Context) {

    private var dbHelper = DBHelper(context)

    fun resetDB() {
        context.deleteDatabase("planman.db")
        dbHelper = DBHelper(context)
    }

    fun insertMyData(data: MyData2): MyData2 {
        val db = dbHelper.writableDatabase
        val cv: ContentValues = data.getContentValues()
        data.id = db.insert("data", null, cv)
        db.close()
        return data
    }

    fun updateMyData(data: MyData2) {
        val db = dbHelper.writableDatabase
        val cv: ContentValues = data.getContentValues()
        db.update("data", cv, "id=?", arrayOf(data.id.toString()))
        db.close()
    }

    fun deleteMyData(data: MyData2) {
        val db = dbHelper.writableDatabase
        db.delete("data", "id=?", arrayOf(data.id.toString()))
        db.close()
    }

    fun readMyData(id: Int): MyData2? {
        var result: MyData2? = null
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM data WHERE `id`=?", arrayOf(id.toString()))
        if (cursor.count == 0) {
            return null
        }

        if (cursor.moveToFirst()) {
            val data = MyData2()
            data.id = cursor.getLong(cursor.getColumnIndex("id"))
            data.title = cursor.getString(cursor.getColumnIndex("title"))
            data.content = cursor.getString(cursor.getColumnIndex("content"))
            data.type = cursor.getString(cursor.getColumnIndex("type"))
            data.attachment.webSite = cursor.getString(cursor.getColumnIndex("att_webSite"))
            data.attachment.phoneNumber = cursor.getString(cursor.getColumnIndex("att_phoneNumber"))
            data.attachment.location = cursor.getString(cursor.getColumnIndex("att_location"))
            data.notification.notifyDateTime =
                cursor.getString(cursor.getColumnIndex("not_notifyDateTime"))
            data.notification.notifyRadius =
                cursor.getString(cursor.getColumnIndex("not_notifyRadius"))
            result = data
        }
        db.close()
        cursor.close()

        return result
    }

    fun readMyData(): ArrayList<MyData2> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM data", null)
        val dataList: ArrayList<MyData2> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                val data = MyData2()
                data.id = cursor.getLong(cursor.getColumnIndex("id"))
                data.title = cursor.getString(cursor.getColumnIndex("title"))
                data.content = cursor.getString(cursor.getColumnIndex("content"))
                data.type = cursor.getString(cursor.getColumnIndex("type"))
                data.attachment.webSite = cursor.getString(cursor.getColumnIndex("att_webSite"))
                data.attachment.phoneNumber =
                    cursor.getString(cursor.getColumnIndex("att_phoneNumber"))
                data.attachment.location = cursor.getString(cursor.getColumnIndex("att_location"))
                data.notification.notifyDateTime =
                    cursor.getString(cursor.getColumnIndex("not_notifyDateTime"))
                data.notification.notifyRadius =
                    cursor.getString(cursor.getColumnIndex("not_notifyRadius"))
                dataList.add(data)
            } while (cursor.moveToNext())
        }
        db.close()
        cursor.close()

        return dataList
    }

    fun insertCategory(category: CategoryData) {
        val db = dbHelper.writableDatabase
        val cv: ContentValues = category.getContentValues()
        category.id = db.insert("category", null, cv)
        db.close()
    }

    fun updateCategory(category: CategoryData, originCategoryType: String?= null) {
        if (originCategoryType != null)
            readMyData().map {
                if (it.type == originCategoryType) {
                    it.type = category.type
                    updateMyData(it)
                }
            }

        val db = dbHelper.writableDatabase
        val cv: ContentValues = category.getContentValues()
        db.update("category", cv, "id=?", arrayOf(category.id.toString()))
        db.close()

    }

    fun deleteCategory(category: CategoryData) {

        readMyData().map {
            if (it.type == category.type) {
                it.type = null
                updateMyData(it)
            }
        }

        val db = dbHelper.writableDatabase
        db.delete("category", "id=?", arrayOf(category.id.toString()))
        db.close()
    }

    fun readCategory(): ArrayList<CategoryData> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM category", null)
        val categoryList: ArrayList<CategoryData> = ArrayList()
        if (cursor.moveToFirst()) {
            do {
                val category = CategoryData()
                category.id = cursor.getLong(cursor.getColumnIndex("id"))
                category.type = cursor.getString(cursor.getColumnIndex("type"))
                category.textSize = cursor.getString(cursor.getColumnIndex("textSize"))
                category.textColor = cursor.getString(cursor.getColumnIndex("textColor"))
                category.textStyle = cursor.getString(cursor.getColumnIndex("textStyle"))
                categoryList.add(category)
            } while (cursor.moveToNext())
        }
        db.close()
        cursor.close()

        return categoryList
    }
}