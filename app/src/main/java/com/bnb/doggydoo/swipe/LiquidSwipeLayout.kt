package com.bnb.doggydoo.swipe

interface LiquidSwipeLayout {
    /** ClipPathProvider provides the path used for clipping. */
    var clipPathProvider: ClipPathProvider
    /** Percentage of the view currently revealed. */
    var currentRevealPercent: Float

    /**
     * Update view to specified reveal percentage.
     * @param percent value should be between 0 and 100 (inclusive).
     */
    fun revealForPercentage(percent: Float): Unit
}