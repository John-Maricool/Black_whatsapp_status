package com.maricoolsapps.blackstatus

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.StaticLayout
import android.text.TextPaint
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

     var img: Bitmap? = null
    lateinit var edit:EditText

    lateinit var parent:RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         edit = findViewById<EditText>(R.id.edit)
        parent = findViewById(R.id.parent)
        val save = findViewById<Button>(R.id.save)

        save.setOnClickListener {
             img = getBitmapFromView()
            if (checkPermissionREAD_EXTERNAL_STORAGE()) {
                val bytes = ByteArrayOutputStream()
                img?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path: String = MediaStore.Images.Media.insertImage(contentResolver, img, "Title", "Black status")
                val imageUri: Uri = Uri.parse(path)
                val waIntent = Intent(Intent.ACTION_SEND)
                waIntent.type = "image/*"
                waIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                startActivity(waIntent)
            } else {
                Log.d("TAg", "Error")
            }
        }
        }

    private fun getBitmapFromView(): Bitmap? {

      /*  val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
*/
        val bitmap = Bitmap.createBitmap(parent.width, parent.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = parent.background
        if (bgDrawable != null) bgDrawable.draw(canvas)
        else canvas.drawColor(Color.WHITE)
        parent.draw(canvas)

        /* canvas.drawPaint(paint)

         val size = 4
         val scale = resources.displayMetrics.density
         val correctSize = size * scale + 0.5f
         val paint2 = TextPaint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)

         paint2.textSize = correctSize
        // paint2.isAntiAlias = true
         paint2.color = Color.WHITE
         val staticLayout = StaticLayout.Builder
                 .obtain(text, 0, 12, paint2, 100)
                 .build()
         //val new_canvas = Canvas(bit)
         //paint2.style = Paint.Style.STROKE
         staticLayout.draw(canvas)
         //canvas.drawText(text, 50F, 50f, paint2)*/
        return bitmap
    }

val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

fun checkPermissionREAD_EXTERNAL_STORAGE(): Boolean {
    val currentAPIVersion = Build.VERSION.SDK_INT
    return if (currentAPIVersion >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                val list = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                showDialog("External storage",
                        list)
            } else {
                ActivityCompat
                        .requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
            }
            false
        } else {
            true
        }
    } else {
        true
    }
}

fun showDialog(msg: String,
               permission: Array<String>) {
    val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
    alertBuilder.setCancelable(true)
    alertBuilder.setTitle("Permission necessary")
    alertBuilder.setMessage("$msg permission is necessary")
    alertBuilder.setPositiveButton("Yes"
    ) { dialog, which ->
        ActivityCompat.requestPermissions(this, permission,
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
    }
    val alert: AlertDialog = alertBuilder.create()
    alert.show()
}
   }