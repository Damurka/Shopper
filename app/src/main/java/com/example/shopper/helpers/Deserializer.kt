package com.example.shopper.helpers

import android.util.Log
import androidx.arch.core.util.Function
import com.example.shopper.models.*
import com.google.firebase.database.DataSnapshot

class ProfileListDeserializer : Function<DataSnapshot, List<Profile>> {

    override fun apply(input: DataSnapshot?): List<Profile> {
        val profiles = mutableListOf<Profile>()

        for (data in input!!.children) {
            //Log.i("Deserializer", data.)
            Log.i("Deserializer", data.value.toString())
            if (data.value is String) {
                break
            }

            val profile = data.getValue(Profile::class.java) as Profile
            profile.key = data.key
            Log.i("Deserializer", profile.name)
            profiles.add(profile)
        }

        return profiles
    }

}

class ProfileDeserializer : Function<DataSnapshot, Profile> {

    override fun apply(input: DataSnapshot?): Profile {
        return input!!.getValue(Profile::class.java) as Profile
    }
}

class FriendsDeserializer : Function<DataSnapshot, List<Friend>> {

    override fun apply(input: DataSnapshot?): List<Friend> {
        val friends = mutableListOf<Friend>()

        for (data in input!!.children) {
            Log.i("Deserializer", data.value.toString())
            if (data.value is String) {
                break
            }

            val friend = data.getValue(Friend::class.java) as Friend
            friend.key = data.key
            Log.i("Deserializer", "Friend Name: " + friend.name + " " + friend.email)
            friends.add(friend)
        }

        return friends
    }

}

class MessageDeserializer : Function<DataSnapshot, List<Message>> {

    override fun apply(input: DataSnapshot?): List<Message> {
        val messages = mutableListOf<Message>()

        for (data in input!!.children) {
            if (data.value is String) {
                break
            }

            val message = data.getValue(Message::class.java) as Message
            message.key = data.key

            messages.add(message)
        }

        return messages
    }

}

class ShoppingListDeserializer : Function<DataSnapshot, List<ShoppingList>> {

    override fun apply(input: DataSnapshot?): List<ShoppingList> {
        val shopping = mutableListOf<ShoppingList>()

        for (data in input!!.children) {
            Log.i("Deserializer", data.value.toString())
            if (data.value is String) {
                break
            }

            val shop = data.getValue(ShoppingList::class.java) as ShoppingList
            shop.key = data.key
            Log.i("Deserializer", shop.name)
            shopping.add(shop)
        }

        return shopping
    }

}

class ShoppingListItemDeserializer : Function<DataSnapshot, ShoppingList> {

    override fun apply(input: DataSnapshot?): ShoppingList {
        val shop = input!!.getValue(ShoppingList::class.java) as ShoppingList
        shop.key = input.key

        return shop
    }

}

class ShoppingDetailDeserializer : Function<DataSnapshot, List<ShoppingItem>> {
    override fun apply(input: DataSnapshot?): List<ShoppingItem> {
        val shopping = mutableListOf<ShoppingItem>()

        for (data in input!!.children) {
            //Log.i("Deserializer", data.)
            Log.i("Deserializer", data.value.toString())
            if (data.value is String) {
                break
            }

            val shop = data.getValue(ShoppingItem::class.java) as ShoppingItem
            shop.key = data.key
            Log.i("Deserializer", shop.name)
            shopping.add(shop)
        }

        return shopping
    }

}
