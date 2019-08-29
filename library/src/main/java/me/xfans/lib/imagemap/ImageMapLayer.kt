package me.xfans.lib.imagemap

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import me.xfans.lib.imagemap.marker.Marker

class ImageMapLayer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var imageView: ImageView? = null
    var markers = mutableListOf<Marker>()


    fun attachToImage(imageView: ImageView) {
        this.imageView = imageView
    }

    fun addMarker(marker: Marker){

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}