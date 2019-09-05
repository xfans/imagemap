package me.xfans.lib.imagemap.sample

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PointF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.alexvasilkov.gestures.GestureController
import com.alexvasilkov.gestures.GestureController.OnStateChangeListener
import com.alexvasilkov.gestures.State
import com.alexvasilkov.gestures.views.GestureImageView
import me.xfans.lib.imagemap.ImageMapLayer
import me.xfans.lib.imagemap.circle.Circle
import me.xfans.lib.imagemap.marker.Marker
import me.xfans.lib.imagemap.polyline.Polyline


class MainActivity : AppCompatActivity() {
    var imageView: GestureImageView? = null
    var imageMap: ImageMapLayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        imageMap = findViewById(R.id.imageMap)

        val ins = assets.open("gugong.jpg")
        val bmp = BitmapFactory.decodeStream(ins)
        imageView?.setImageBitmap(bmp);

        imageMap?.attachToImage(imageView!!);

        imageView?.controller?.addOnStateChangeListener(object : OnStateChangeListener {
            override fun onStateChanged(state: State) {
                imageMap?.update()
            }

            override fun onStateReset(oldState: State, newState: State) {
                imageMap?.update()
            }
        })
        imageView?.controller?.setOnGesturesListener(object : GestureController.OnGestureListener {
            override fun onDown(event: MotionEvent) {

            }

            override fun onDoubleTap(event: MotionEvent): Boolean {
                return false
            }

            override fun onUpOrCancel(event: MotionEvent) {
            }

            override fun onLongPress(event: MotionEvent) {
            }

            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                return false
            }

            override fun onSingleTapUp(event: MotionEvent): Boolean {
                return imageMap?.click(event) == true
            }

        })

        val marker = Marker(1, 773f, 1834f, getIcon(R.color.colorAccent))
        val marker1 = Marker(2, 388f, 788f, getIcon(R.color.colorPrimary))

        val lists = mutableListOf<PointF>()
        lists.add(PointF(773f, 1834f))

        lists.add(PointF(773f, 1717f))
        lists.add(PointF(640f, 1717f))
        lists.add(PointF(640f, 1610f))
        lists.add(PointF(510f, 1610f))
        lists.add(PointF(508f, 1491f))
        lists.add(PointF(517f, 1341f))

        lists.add(PointF(542f, 1286f))
        lists.add(PointF(542f, 854f))
        lists.add(PointF(388f, 854f))

        lists.add(PointF(388f, 788f))
        val polyline = Polyline(1, lists)
        imageMap?.addPolyline(polyline)
        imageMap?.addMarker(marker)
        imageMap?.addMarker(marker1)


        val marker2 = Marker(1, 773f, 1191f, getIcon(R.color.colorAccent))
        val marker3 = Marker(2, 1500f, 1550f, getIcon(R.color.colorPrimary))
        var lists1 = mutableListOf<PointF>()
        lists1.add(PointF(773f, 1191f))
        lists1.add(PointF(1157f, 1191f))
        lists1.add(PointF(1157f, 1231f))
        lists1.add(PointF(1274f, 1231f))
        lists1.add(PointF(1274f, 1540f))
        lists1.add(PointF(1500f, 1550f))
        var polyline1 = Polyline(1, lists1, true)
        imageMap?.addPolyline(polyline1)
        imageMap?.addMarker(marker2)
        imageMap?.addMarker(marker3)

        var circle = Circle(0, PointF(773f,1191f),200f,Color.parseColor("#7A0000AA"))
        imageMap?.addCircles(circle)

    }

    private fun getIcon(@ColorRes colorId: Int): Drawable {
        var icon: Drawable? =
            ContextCompat.getDrawable(this, R.drawable.ic_place_white_24dp) ?: throw NullPointerException()
        icon = DrawableCompat.wrap(icon!!)
        DrawableCompat.setTint(icon!!, ContextCompat.getColor(this, colorId))
        return icon
    }


}
