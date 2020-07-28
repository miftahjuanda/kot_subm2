package com.kelaspemula.submission1.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var photo: String? = null,
    var name: String? = null,
    var type: String? = null,
    var location: String? = null,
    var avatar: String? = null,
    var username: String? = null,
    var login : String? = null,
    var company: String? = null
) : Parcelable