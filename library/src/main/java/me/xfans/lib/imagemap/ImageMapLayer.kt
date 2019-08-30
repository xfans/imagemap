package me.xfans.lib.imagemap

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import me.xfans.lib.imagemap.marker.Marker
import me.xfans.lib.imagemap.polyline.Polyline
import android.graphics.DashPathEffect
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class ImageMapLayer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var imageView: ImageView? = null
    var markers = mutableListOf<Marker>()
    var polylines = mutableListOf<Polyline>()
    val pointIn = FloatArray(2)
    var pointOut = FloatArray(2)
    val posRect = Rect()
    val iconRect = Rect()
    var iconMatrix = Matrix()

    var polylinePaint = Paint()
    val polylinePath = Path()

    fun attachToImage(imageView: ImageView) {
        this.imageView = imageView
    }

    fun addMarker(marker: Marker) {
        markers.add(marker)
        invalidate()
    }

    fun addPolyline(polyline: Polyline) {
        polylines.add(polyline)
        invalidate()
    }

    fun update() {
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (polyline in polylines) {
            drawPolyline(polyline, canvas)
        }
        for (maker in markers) {
            drawMaker(maker, canvas)
        }
    }

    private fun drawPolyline(polyline: Polyline, canvas: Canvas?) {
        polylinePath.reset()
        polylinePaint.color = Color.RED
        polylinePaint.style = Paint.Style.STROKE;
        polylinePaint.strokeWidth = 10f;
        polylinePaint.strokeJoin = Paint.Join.ROUND;
        polylinePaint.strokeCap = Paint.Cap.ROUND;
        polylinePaint.isAntiAlias = true
        if (polyline.isDashLine) {
            val dashPathEffect1 = DashPathEffect(floatArrayOf(20f, 20f), 0f)
            polylinePaint.setPathEffect(dashPathEffect1)
        } else {
            polylinePaint.setPathEffect(null)
        }

        val lists = polyline.points
        for (i in lists.indices) {
            val point = lists[i]
            pointIn[0] = point.x
            pointIn[1] = point.y
            imageView?.imageMatrix?.mapPoints(pointOut, pointIn)
            if (i == 0) {
                polylinePath.moveTo(pointOut[0], pointOut[1])
            } else {
                polylinePath.lineTo(pointOut[0], pointOut[1])
            }
        }
        canvas?.save()
        canvas?.drawPath(polylinePath, polylinePaint)
        canvas?.restore()
    }

    private fun drawMaker(maker: Marker, canvas: Canvas?) {
        val icon = maker.icon
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