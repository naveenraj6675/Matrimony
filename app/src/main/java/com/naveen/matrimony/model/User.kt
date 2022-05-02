package com.naveen.matrimony.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import coil.load
import coil.request.CachePolicy
import com.naveen.matrimony.R


@Entity
class User {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "profileImgList")
    var profileImgList: ArrayList<String>? = null

    @ColumnInfo(name = "address")
    var address: String? = null

    @ColumnInfo(name = "isNew")
    var isNew: Boolean? = null

    @ColumnInfo(name = "isVerified")
    var isVerified: Boolean? = null

    @ColumnInfo(name = "isPremium")
    var isPremium: Boolean? = null

    constructor(
        id: Int?,
        name: String?,
        profileImgList: ArrayList<String>?,
        address: String?,
        isNew: Boolean?,
        isVerified: Boolean?,
        isPremium: Boolean?
    ) {
        this.id = id
        this.name = name
        this.profileImgList = profileImgList
        this.address = address
        this.isNew = isNew
        this.isVerified = isVerified
        this.isPremium = isPremium
    }

    companion object {
        @BindingAdapter("bind:src")
        @JvmStatic
        fun setImageResource(imageView: ImageView, resource: String) {
            imageView.load(resource) {
                placeholder(R.drawable.ic_placeholder)
                crossfade(true)
                memoryCachePolicy(CachePolicy.ENABLED)
            }
        }
    }


}



