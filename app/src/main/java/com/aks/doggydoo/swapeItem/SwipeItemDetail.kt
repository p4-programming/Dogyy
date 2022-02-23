package com.aks.doggydoo.swapeItem

import android.graphics.Bitmap
import android.graphics.RectF

class SwipeItemDetail {
    var id: Int = -1
    var buttonState: SwipeState? = null
    var backgroundColor : String ? = "#FFFFFF"
    var backgroundImage : Bitmap? = null
    var iconBitmap : Bitmap? = null
    var iconWidth : Int = 0
    var iconHeight: Int = 0
    var textHeight = 10f
    var text : String ? = ""
    var textColor : String ="#000000"
    var clickableArea: RectF? = null
}