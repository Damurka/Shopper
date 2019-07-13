package com.example.shopper

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class PhotoFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity())
            .setItems(R.array.photo) { _, which ->
                val intent = Intent()
                intent.putExtra(PHOTO_EXTRA, which)

                targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)
            }
            .create()
    }

    companion object {
        const val PHOTO_EXTRA = "com.example.recipes.PHOTO_EXTRA"
    }
}