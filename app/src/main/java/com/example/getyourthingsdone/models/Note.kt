package com.example.getyourthingsdone.models

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Note(var title: String, var description: String?, var date: Calendar?) : Parcelable {


}