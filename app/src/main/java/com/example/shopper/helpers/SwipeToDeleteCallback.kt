package com.example.shopper.helpers

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shopper.R


class SwipeToDeleteCallback(private val listener: (Int) -> Unit) : ItemTouchHelper.Callback() {

    var isOwner = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return if (isOwner) makeMovementFlags(0, ItemTouchHelper.RIGHT) else 0
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
            val itemHeight = v.bottom - v.top

            drawDeleteBackground(v, c, dX)
            drawDeleteIcon(v, c, itemHeight)
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawDeleteIcon(view: View, canvas: Canvas, itemHeight: Int) {
        val deleteIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_delete) as Drawable
        val iconWidth = deleteIcon.intrinsicWidth
        val iconHeight = deleteIcon.intrinsicHeight

        val deleteIconMargin = (itemHeight - iconHeight) / 2
        val deleteIconTop = view.top + deleteIconMargin
        val deleteIconRight = view.left + iconWidth + deleteIconMargin
        val deleteIconLeft = view.left + deleteIconMargin
        val deleteIconBottom = deleteIconTop + iconHeight

        deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
        deleteIcon.draw(canvas)
    }

    private fun drawDeleteBackground(view: View, canvas: Canvas, dX: Float) {
        val background = ColorDrawable()
        background.color = Color.parseColor("#f44336")

        background.setBounds(view.left, view.top, view.left + dX.toInt(), view.bottom)
        background.draw(canvas)
    }
}