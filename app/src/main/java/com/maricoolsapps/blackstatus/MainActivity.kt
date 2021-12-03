package com.maricoolsapps.blackstatus

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

     var img: Bitmap? = null
    lateinit var edit: EditText
    var color_click = 0

    val colors = listOf(
            R.color.white,
        R.color.green,
        R.color.red,
        R.color.teal_200,
        R.color.teal_700,
        R.color.grey,
        R.color.d_blue,
        R.color.l_purple,
        R.color.orange,
        R.color.yellow)

    lateinit var title: TextView
    lateinit var parent:ConstraintLayout
    lateinit var color_btn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = findViewById(R.id.title)
         edit = findViewById(R.id.edit)
        parent = findViewById(R.id.parent)
        color_btn = findViewById(R.id.text_color_change_btn)
        val save = findViewById<ImageView>(R.id.save)

        color_btn.setOnClickListener {
                edit.setTextColor(resources.getColor(colors[color_click], null))
            color_click++
            if (color_click > colors.lastIndex){
                color_click = 0
            }
        }

        save.setOnClickListener {
                title.visibility = View.GONE
                save.visibility = View.GONE
                color_btn.visibility = View.GONE
                img = getBitmapFromView()
            color_btn.visibility = View.VISIBLE
                title.visibility = View.VISIBLE
                save.visibility = View.VISIBLE

                if (checkPermissionREAD_EXTERNAL_STORAGE()) {
                    val bytes = ByteArrayOutputStream()
                    img?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path: String = MediaStore.Images.Media.insertImage(
                        contentResolver,
                        img,
                        "Title",
                        "Black status"
                    )
                    val imageUri: Uri = Uri.parse(path)
                    val waIntent = Intent(Intent.ACTION_SEND)
                    waIntent.type = "image/*"
                    waIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
                    startActivity(waIntent)
                } else {
                }
            }
        }

    private fun getBitmapFromView(): Bitmap? {
        val v1: View = window.decorView.rootView
        val bitmap = Bitmap.createBitmap(v1.width, v1.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val bgDrawable = parent.background
        if (bgDrawable != null) bgDrawable.draw(canvas)
        else canvas.drawColor(Color.WHITE)
        v1.draw(canvas)
        return bitmap
    }

val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

private fun checkPermissionREAD_EXTERNAL_STORAGE(): Boolean {
    val currentAPIVersion = Build.VERSION.SDK_INT
    return if (currentAPIVersion >= Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )) {
                val list = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                showDialog(
                    "Storage",
                    list
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            }
            false
        } else {
            true
        }
    } else {
        true
    }
}

fun showDialog(msg: String, permission: Array<String>) {
    val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
    alertBuilder.setCancelable(true)
    alertBuilder.setTitle("Permission necessary")
    alertBuilder.setMessage("$msg permission is necessary")
    alertBuilder.setPositiveButton(
        "Yes"
    ) { dialog, which ->
        ActivityCompat.requestPermissions(
            this, permission,
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
        )
    }
    val alert: AlertDialog = alertBuilder.create()
    alert.show()
}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
   }