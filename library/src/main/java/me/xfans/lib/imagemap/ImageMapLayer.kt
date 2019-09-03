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
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import me.xfans.lib.imagemap.circle.Circle
import me.xfans.lib.imagemap.marker.OnMarkerClickListener


class ImageMapLayer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var imageView: ImageView? = null
    var markers = mutableListOf<Marker>()
    var polylines = mutableListOf<Polyline>()
    var circles = mutableListOf<Circle>()
    val pointIn = FloatArray(2)
    var pointOut = FloatArray(2)
    val posRect = Rect()
    val iconRect = Rect()
    var iconMatrix = Matrix()

    var polylinePaint = Paint()
    val polylinePath = Path()
    var values = FloatArray(9)

    private var mOnMarkerClickListener: OnMarkerClickListener? = null

    fun setOnMarkerClickListener(onMarkerClickListener: OnMarkerClickListener) {
        mOnMarkerClickListener = onMarkerClickListener
    }

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

    fun addCircles(circle: Circle) {
        circles.add(circle)
        invalidate()
    }

    fun update() {
        invalidate()
    }

    fun click(event: MotionEvent): Boolean {
        Log.e("ImageMapLayer", "x:" + event.x + " y:" + event.y)
        imageView?.imageMatrix?.getValues(values)

        val relativeX = (event.x - values[2]) / values[0]
        val relativeY = (event.y - values[5]) / values[4]

        for (m in markers) {
            if (inMarker(relativeX, relativeY, m)) {
                Toast.makeText(context, "id:" + m.id, Toast.LENGTH_LONG).show()
                if (mOnMarkerClickListener != null) {
                    mOnMarkerClickListener?.click(m)
                }
                return true
            }
        }
        return false
    }

    private fun inMarker(x: Float, y: Float, m: Marker): Boolean {
        val icon = m.icon
        val w = icon.intrinsicWidth / 2
        val h = icon.intrinsicHeight / 2
        val clickRect = Rect(x.toInt(), y.toInt(), (x + 1).toInt(), (y + 1).toInt())
        val markRect =
            Rect((m.x - w - 5).toInt(), (m.y - h - 5).toInt(), (m.x + w + 5).toInt(), (m.y + h + 5).toInt())
        return clickRect.intersect(markRect)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (circle in circles) {
            drawCircle(circle, canvas)
        }
        for (polyline in polylines) {
            drawPolyline(polyline, canvas)
        }
        for (maker in markers) {
            drawMaker(maker, canvas)
        }
    }

    private fun drawCircle(circle: Circle, canvas: Canvas?) {
        //TODO drawCircle
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

        iconRect.set(0, 0, icon.intrinsicWidth, icon.intrinsicHeight);

        Gravity.apply(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0, iconRect, posRect);

        iconMatrix.postTranslate((-posRect.left).toFloat(), (-posRect.top).toFloat())

        canvas?.save();

        canvas?.concat(iconMatrix)
        var width = icon.getIntrinsicWidth();
        var height = icon.getIntrinsicHeight();
        icon.setBounds(0, 0, width, height)
        icon.draw(canvas!!)

        canvas.restore();
    }
}