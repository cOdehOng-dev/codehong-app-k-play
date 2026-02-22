package com.codehong.app.kplay.manager

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface

object BitmapManager {

    fun createBadgeOverlayBitmap(count: Int): Bitmap {
        val widthPx = 80
        val heightPx = 108  // 메인 핀 비율(32dp × 44dp ≈ 1 : 1.35)
        val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // 뱃지 원 위치: 비트맵 우측(80%) 상단(20%) — 핀 머리 우측 상단 코너에 해당
        val badgeRadius = widthPx * 0.20f
        val badgeCenterX = widthPx * 0.80f
        val badgeCenterY = widthPx * 0.20f

        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.RED
            style = Paint.Style.FILL
        }
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = widthPx * 0.06f
        }

        canvas.drawCircle(badgeCenterX, badgeCenterY, badgeRadius, fillPaint)
        canvas.drawCircle(badgeCenterX, badgeCenterY, badgeRadius, borderPaint)

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = android.graphics.Color.WHITE
            textSize = widthPx * 0.18f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        val text = if (count > 9) "9+" else count.toString()
        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        canvas.drawText(text, badgeCenterX, badgeCenterY + textBounds.height() / 2f, textPaint)

        return bitmap
    }

}