package uz.isystem.newsapplication.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterModel(
    val q : String,
    val searchIn : String?,
    val sortBy : String?,
    val from : String?,
    val to : String?,
    val isBack : Boolean
):Parcelable
