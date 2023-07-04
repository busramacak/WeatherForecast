package com.bmprj.weatherforecast.data.db

import android.annotation.SuppressLint
import android.content.ContentValues
import com.bmprj.weatherforecast.model.Search

class DAO {

    fun add(dh: DatabaseHelper, id:Int, search:String?){
        val db = dh.writableDatabase
        val values =ContentValues()
        values.put("id",id)
        values.put("search",search)

        db.insertOrThrow("weatherSearch",null,values)
        db.close()
    }


    fun update(dh: DatabaseHelper, id:Int, search: String?){
        val db = dh.writableDatabase
        val values = ContentValues()
        values.put("search",search)

        db.update("weatherSearch",values,"id=?", arrayOf(id.toString()))
        db.close()
    }


    @SuppressLint("Range")
    fun get(dh : DatabaseHelper):ArrayList<Search>{
        val searchList = ArrayList<Search>()
        val db = dh.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM weatherSearch",null)

        while(cursor.moveToNext()){
            val search = Search(cursor.getInt(cursor.getColumnIndex("id")),
            cursor.getString(cursor.getColumnIndex("search")))

            searchList.add(search)
        }
        return searchList
    }

    fun delete(dh: DatabaseHelper){
        val db = dh.writableDatabase
        db.delete("weatherSearch",null,null)
    }
}