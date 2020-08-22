package com.kelaspemula.submission1.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.kelaspemula.submission1.db.DatabaseContract.UserColums.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbusergithub"
        private const val DATABASE_VERSION = 1

        private val SQL_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.UserColums._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${DatabaseContract.UserColums.PHOTO} TEXT NOT NULL," +
                "${DatabaseContract.UserColums.NAME} TEXT NOT NULL," +
                "${DatabaseContract.UserColums.TYPE} TEXT NOT NULL)"
        /* "${DatabaseContract.UserColums.LOCATION} TEXT NOT NULL," +
         "${DatabaseContract.UserColums.AVATAR} TEXT NOT NULL," +
         "${DatabaseContract.UserColums.USERNAME} TEXT NOT NULL," +
         "${DatabaseContract.UserColums.LOGIN} TEXT NOT NULL," +
         "${DatabaseContract.UserColums.COMPANY} TEXT NOT NULL)"*/
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}