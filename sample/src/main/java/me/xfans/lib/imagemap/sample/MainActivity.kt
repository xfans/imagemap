package me.xfans.lib.imagemap.sample

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
import com.alexvasilkov.gestures.State


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

        imageView?.getController()?.addOnStateChangeListener(object : OnStateChangeListener {
            override fun onStateChanged(state: State) {
                imageMap?.update()
            }

            override fun onStateReset(oldState: State, newState: State) {
                imageMap?.update()
            }
        })
        var marker = Marker(1, 1000f, 200f, getIcon(R.color.colorAccent))
        var marker1 = Marker(1, 1500f, 600f, getIcon(R.color.colorPrimary))

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
