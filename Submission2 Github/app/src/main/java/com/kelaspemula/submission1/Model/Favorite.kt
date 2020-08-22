package com.kelaspemula.submission1.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite (
    var id: Int = 0,
    var photo: String? = null,
    var name: String? = null,
    var type: String? = null
) : Parcelable