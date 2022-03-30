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
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.maricoolsapps.blackstatus.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    var img: Bitmap? = null
    var color_click = 0

    // private val model: MainViewModel by viewModels()

    val colors = listOf(
        R.color.green,
        R.color.red,
        R.color.teal_200,
        R.color.teal_700,
        R.color.grey,
        R.color.d_blue,
        R.color.l_purple,
        R.color.orange,
        R.color.yellow,
        R.color.white
        )
    val sizes = listOf(
        45f,
        60f,
        25f,
        38f
        )

    var sizeClick = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.textColorChangeBtn?.setOnClickListener {
            binding.edi?.setTextColor(resources.getColor(colors[color_click], null))
            color_click++
            if (color_click > colors.lastIndex) {
                color_click = 0
            }
        }
       binding.textSizeChange?.setOnClickListener {
            binding.edi?.textSize = sizes[sizeClick]
            sizeClick++
            if (sizeClick > sizes.lastIndex){
                sizeClick = 0
            }
        }

    /*    binding.slider?.addOnChangeListener { slider, value, _->
            Log.d("sajn", value.toString())
            Log.d("sajn", slider.value.toString())
            binding.edi?.textSize = value
        }

*/
        binding.save.setOnClickListener {
            if (binding.edi?.text.toString().isEmpty()) {
                return@setOnClickListener
            } else {
                img = getBitmapFromView()
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
    }

    private fun getBitmapFromView(): Bitmap? {
        val bitmap = Bitmap.createBitmap(
            binding.edi?.width!!,
            binding.edi?.height!!,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val bgDrawable = binding.edi?.background
        bgDrawable?.draw(canvas)
        binding.edi?.draw(canvas)
        return bitmap
    }

    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123

    private fun checkPermissionREAD_EXTERNAL_STORAGE(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) && ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
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