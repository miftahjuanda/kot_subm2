package com.kelaspemula.submission1.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.kelaspemula.submission1.db.DatabaseContract.UserColums.Companion.NAME
import com.kelaspemula.submission1.db.DatabaseContract.UserColums.Companion.TABLE_NAME
import com.kelaspemula.submission1.db.DatabaseContract.UserColums.Companion._ID
import java.sql.SQLException

class UserHelper(context: Context) {

    companion object {
        private val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: UserHelper? = null
        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun isOpen(): Boolean {
        return try {
            database.isOpen
        } catch (e: Exception) {
            false
        }
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE, null, null, null, null, null, "$_ID ASC", null
        )
    }

    fun queryById(id: String): Cursor {
        return database.query(
            DATABASE_TABLE, null, "$NAME = ?", arrayOf(id), null, null, null, null
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$NAME = ?", arrayOf(id))
    }

    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$NAME = '$id'", null)
    }
}