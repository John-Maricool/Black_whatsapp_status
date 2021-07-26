package com.maricoolsapps.blackstatus

import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.text.StaticLayout
import android.text.TextPaint
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

     var img: Bitmap? = null
    lateinit var edit:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         edit = findViewById<EditText>(R.id.edit)
        val save = findViewById<Button>(R.id.save)

        save.setOnClickListener {
             img = getBitmapFromView(edit.text.toString())
            val waIntent = Intent(this, MainActivity2::class.java)
            waIntent.type = "image/*"
            // waIntent.setPackage(packageManager.toString())
            waIntent.putExtra("Img", img)
            startActivity(waIntent)

        }
    }

    private fun getBitmapFromView(text: String): Bitmap? {

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK

        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawPaint(paint)
        val size = 4
        val scale = resources.displayMetrics.density
        val correctSize = size * scale + 0.5f
        val paint2 = TextPaint()

        paint2.textSize = correctSize
        paint2.isAntiAlias = true
        paint2.color = Color.WHITE
        val staticLayout = StaticLayout.Builder
                .obtain(text, 0, 1, paint2, 20)
                .build()
        //paint2.style = Paint.Style.STROKE
        staticLayout.draw(canvas)
        //canvas.drawText(text, 50F, 50f, paint2)
        return bitmap
    }

   }