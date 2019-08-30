package me.xfans.lib.imagemap.polyline

import android.graphics.Point
import android.graphics.PointF

data class Polyline(val id: Int, val points: List<PointF>, var isDashLine: Boolean = false)