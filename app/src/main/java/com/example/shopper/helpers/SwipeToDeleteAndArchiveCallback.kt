package com.example.shopper.helpers

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shopper.R

interface OnSwipe {
    fun delete(position: Int)
    fun archive(position: Int)
}

class SwipeToDeleteAndArchiveCallback(private val listener: OnSwipe) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.LEFT) {
            listener.archive(viewHolder.adapterPosition)
        } else if (direction == ItemTouchHelper.RIGHT) {
            listener.delete(viewHolder.adapterPosition)
        }
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

            drawArchiveBackground(v, c, dX)
            drawArchiveIcon(v, c, itemHeight)
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

    private fun drawArchiveIcon(view: View, canvas: Canvas, itemHeight: Int) {
        val archiveIcon = ContextCompat.getDrawable(view.context, R.drawable.ic_archive) as Drawable
        val iconWidth = archiveIcon.intrinsicWidth
        val iconHeight = archiveIcon.intrinsicHeight

        val archiveIconMargin = (itemHeight - iconHeight) / 2
        val archiveIconTop = view.top + archiveIconMargin
        val archiveIconRight = view.right - archiveIconMargin
        val archiveIconLeft =archiveIconRight - iconWidth
        val archiveIconBottom = archiveIconTop + iconHeight

        archiveIcon.setBounds(archiveIconLeft, archiveIconTop, archiveIconRight, archiveIconBottom)
        archiveIcon.draw(canvas)
    }

    private fun drawDeleteBackground(view: View, canvas: Canvas, dX: Float) {
        val background = ColorDrawable()
        background.color = Color.parseColor("#f44336")

        background.setBounds(view.left, view.top, view.left + dX.toInt(), view.bottom)
        background.draw(canvas)
    }

    private fun drawArchiveBackground(view: View, canvas: Canvas, dX: Float) {
        val background = ColorDrawable()
        background.color = Color.parseColor("#327531")

        background.setBounds(view.right + dX.toInt(), view.top, view.right, view.bottom)
        background.draw(canvas)
    }
}