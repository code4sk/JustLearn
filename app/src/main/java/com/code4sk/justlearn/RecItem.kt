package com.code4sk.justlearn

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
data class RecItem(val file: File) : Parcelable