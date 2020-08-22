package com.kelaspemula.submission1.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTH = "com.kelaspemula.submission1"
    const val SCHEME = "content"

    internal class UserColums : BaseColumns {
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val PHOTO = "photo"
            const val NAME = "name"
            const val TYPE = "type"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTH)
                .appendPath(TABLE_NAME)
                .build()

            /*const val LOCATION = "location"
            const val AVATAR = "avatar"
            const val USERNAME = "username"
            const val LOGIN = "login"
            const val COMPANY = "company"*/
        }
    }
}