package com.bnb.doggydoo.parkdescription.utility

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class OverlappingItem : RecyclerView.ItemDecoration() {

    private val vertOverlap = -100
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.set(0, 0, vertOverlap, 0)
    }
}