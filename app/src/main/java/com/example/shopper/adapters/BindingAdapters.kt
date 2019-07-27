package com.example.shopper.adapters

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

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

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}