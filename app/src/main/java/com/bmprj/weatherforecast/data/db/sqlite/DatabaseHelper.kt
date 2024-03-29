package com.bmprj.weatherforecast.data.db.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DataBase private constructor(){

    companion object{
        private var instance: DatabaseHelper?=null

        fun getInstance(context: Context): DatabaseHelper {
            if(instance ==null){
                instance = DatabaseHelper(context)
            }
            return instance!!
        }

    }
}
class DatabaseHelper(context: Context):SQLiteOpenHelper(context,"weather",null,14) {
    override fun onCreate(p0: SQLiteDatabase?) { p0?.execSQL("CREATE TABLE weatherSearch (id INTEGER, search TEXT)")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS weaterSearch")
    }

}