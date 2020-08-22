package com.kelaspemula.submission1.helper

import android.database.Cursor
import com.kelaspemula.submission1.Model.User
import com.kelaspemula.submission1.db.DatabaseContract

object MappingHelper {
    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<User>{
        val favList = ArrayList<User>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColums._ID))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.UserColums.PHOTO))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColums.NAME))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.UserColums.TYPE))
                favList.add(User(id, photo, name, type))
            }
        }
        return favList
    }

    fun mapCursorToObject(cursor: Cursor?): User {
        var fav = User()
        cursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColums._ID))
            val photo = getString(getColumnIndexOrThrow(DatabaseContract.UserColums.PHOTO))
            val name = getString(getColumnIndexOrThrow(DatabaseContract.UserColums.NAME))
            val type = getString(getColumnIndexOrThrow(DatabaseContract.UserColums.TYPE))
            fav = User(id, photo, name, type)
        }
        return fav
    }
}