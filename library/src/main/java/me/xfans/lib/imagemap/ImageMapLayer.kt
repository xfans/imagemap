package me.xfans.lib.imagemap

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import me.xfans.lib.imagemap.marker.Marker
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.Gravity
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class ImageMapLayer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var imageView: ImageView? = null
    var markers = mutableListOf<Marker>()
    val pointIn = FloatArray(2)
    var pointOut = FloatArray(2)
    val posRect = Rect()
    val iconRect = Rect()
    var iconMatrix = Matrix()

    fun attachToImage(imageView: ImageView) {
        this.imageView = imageView
    }

    fun addMarker(marker: Marker) {
        markers.add(marker)
        invalidate()
    }

    fun update() {
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (maker in markers) {
            drawMaker(maker, canvas)
        }
    }

    private fun drawMaker(maker: Marker, canvas: Canvas?) {
        var icon = maker.icon
        pointIn[0] = maker.x
        pointIn[1] = maker.y
        iconMatrix.reset()
        imageView?.imageMatrix?.mapPoints(pointOut, pointIn)
        iconMatrix.postTranslate(pointOut[0], pointOut[1])

        iconRect.set(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        
        Gravity.apply(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0, iconRect, posRect);


        iconMatrix.postTranslate((-posRect.left).toFloat(), (-posRect.top).toFloat());
        canvas?.save();

        canvas?.concat(iconMatrix)
        var width = icon.getIntrinsicWidth();
        var height = icon.getIntrinsicHeight();
        icon.setBounds(0, 0, width, height)
        icon.draw(canvas!!)

        canvas.restore();
    }
}