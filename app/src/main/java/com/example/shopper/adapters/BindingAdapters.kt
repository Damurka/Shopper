package com.example.shopper.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator

@BindingAdapter("imageFromText")
fun imageFromText(imageView: ImageView, text: String) {
    val generator =  ColorGenerator.MATERIAL
    val builder = TextDrawable.builder()
        .beginConfig()
        .withBorder(4)
        .endConfig()
        .roundRect(8)

    val drawable = builder.build(text.first().toString(), generator.getColor(text))
    imageView.setImageDrawable(drawable)
}