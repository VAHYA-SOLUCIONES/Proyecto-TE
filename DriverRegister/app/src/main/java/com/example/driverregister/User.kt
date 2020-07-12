package com.example.driverregister

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize //No sé que rayos hace esto
class User(val uid: String, val nombre_alfil: String, val profileImageUrl: String): Parcelable {
    constructor() : this("", "", "")
}