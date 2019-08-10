package com.example.shopper.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Recipe(
    @SerializedName("label")
    var label: String? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("ingredientLines")
    var ingredients: List<String> = listOf(),

    @SerializedName("yield")
    var yield: Int = 0,

    @SerializedName("calories")
    var calories: Float = 0f,

    @SerializedName("source")
    var source: String? = null,

    @SerializedName("url")
    var url: String? = null
) : Parcelable {

    constructor(parcel: Parcel): this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList() as List<String>,
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(label)
        parcel.writeString(image)
        parcel.writeStringList(ingredients)
        parcel.writeInt(this.yield)
        parcel.writeFloat(calories)
        parcel.writeString(source)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Recipe> {
        override fun createFromParcel(parcel: Parcel): Recipe {
            return Recipe(parcel)
        }

        override fun newArray(size: Int): Array<Recipe?> {
            return arrayOfNulls(size)
        }
    }
}

data class RecipeCover(
    @SerializedName("recipe")
    var recipe: Recipe? = null,

    @SerializedName("bookmarked")
    var bookmarked: Boolean = false,

    @SerializedName("bought")
    var bought: Boolean = false
)