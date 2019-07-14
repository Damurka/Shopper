package com.example.shopper.helpers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shopper.R
import kotlin.math.abs

class ShopperSwipeToDeleteCallback(private val listener: (Int) -> Unit) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener(viewHolder.adapterPosition)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val v = viewHolder.itemView
            c.clipRect(0f, v.top.toFloat(), dX, v.bottom.toFloat())
            if (dX < recyclerView.width / 3)
                c.drawColor(Color.GRAY)
            else
                c.drawColor(Color.RED)

            val icon = ContextCompat.getDrawable(recyclerView.context, R.drawable.ic_delete) as Drawable
            icon.bounds = Rect(16, v.top + 16, icon.intrinsicWidth + 16, v.top + icon.intrinsicWidth + 16)
            icon.draw(c)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}