package me.xfans.lib.imagemap.sample

import android.graphics.Point
import android.graphics.PointF
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.alexvasilkov.gestures.views.GestureImageView
import me.xfans.lib.imagemap.ImageMapLayer
import me.xfans.lib.imagemap.marker.Marker
import com.alexvasilkov.gestures.GestureController.OnStateChangeListener
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.MotionEvent
import com.alexvasilkov.gestures.GestureController
import com.alexvasilkov.gestures.State
import me.xfans.lib.imagemap.polyline.Polyline


class MainActivity : AppCompatActivity() {
    var imageView: GestureImageView? = null
    var imageMap: ImageMapLayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        imageMap = findViewById(R.id.imageMap)
        imageView?.setImageResource(R.drawable.world_map);
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

        val marker = Marker(1, 1000f, 200f, getIcon(R.color.colorAccent))
        val marker1 = Marker(2, 1500f, 600f, getIcon(R.color.colorPrimary))

        val lists = mutableListOf<PointF>()
        lists.add(PointF(1000f, 200f))

        lists.add(PointF(1500f, 600f))
        val polyline = Polyline(1, lists)
        imageMap?.addPolyline(polyline)

        var lists1 = mutableListOf<PointF>()
        lists1.add(PointF(1200f, 1200f))
        lists1.add(PointF(1100f, 1300f))
        lists1.add(PointF(1100f, 1400f))
        lists1.add(PointF(1500f, 1800f))
        lists1.add(PointF(1800f, 1200f))
        lists1.add(PointF(2000f, 1500f))
        var polyline1 = Polyline(1, lists1, true)
        imageMap?.addPolyline(polyline1)

        imageMap?.addMarker(marker)
        imageMap?.addMarker(marker1)
    }

    private fun getIcon(@ColorRes colorId: Int): Drawable {
        var icon: Drawable? =
            ContextCompat.getDrawable(this, R.drawable.ic_place_white_24dp) ?: throw NullPointerException()
        icon = DrawableCompat.wrap(icon!!)
        DrawableCompat.setTint(icon!!, ContextCompat.getColor(this, colorId))
        return icon
    }
}
