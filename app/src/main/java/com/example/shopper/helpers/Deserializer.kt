package com.example.shopper.helpers

import android.util.Log
import androidx.arch.core.util.Function
import com.example.shopper.models.Profile
import com.example.shopper.models.ShoppingList
import com.google.firebase.database.DataSnapshot

class ProfileDeserializer : Function<DataSnapshot, Profile> {

    override fun apply(input: DataSnapshot?): Profile {
        return input!!.getValue(Profile::class.java) as Profile
    }
}

class ShoppingListDeserializer : Function<DataSnapshot, List<ShoppingList>> {

    override fun apply(input: DataSnapshot?): List<ShoppingList> {
        val shopping = mutableListOf<ShoppingList>()

        for (data in input!!.children) {
            //Log.i("Deserializer", data.)
            Log.i("Deserializer", data.value.toString())
            if (data.value is String) {
                break
            }

            val shop = data.getValue(ShoppingList::class.java) as ShoppingList
            Log.i("Deserializer", shop.name)
            shopping.add(shop)
        }

        return shopping
    }

}

/*
class CategoryDeserializer : Function<DataSnapshot, Category> {

    override fun apply(input: DataSnapshot?): Category {
        return input!!.getValue(Category::class.java) as Category
    }
}
*/