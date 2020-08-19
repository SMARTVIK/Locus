package com.vik.locusassignment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var selectedPostion: Int ? = -1
    private lateinit var map: java.util.HashMap<Int, Bitmap>
    private lateinit var selectedImage: ImageView
    private val CAMERA_REQUEST: Int = 1
    private val MY_CAMERA_PERMISSION_CODE: Int = 100
    private var objectArray: AssignmentModel? = null
    private var adapter : ItemsAdapter ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFromAssets()
        setUpList()
    }

    private fun setUpList() {
        val layoutManager = LinearLayoutManager(this).apply { orientation = RecyclerView.VERTICAL }
        rvSearchList.layoutManager = layoutManager
        adapter = ItemsAdapter(this)
        rvSearchList.adapter = adapter
        adapter!!.setData(objectArray)
    }

    private fun loadFromAssets() {
        val objectArrayString: String = resources.openRawResource(R.raw.assignment).bufferedReader().use { it.readText() }
        objectArray = Gson().fromJson(objectArrayString, AssignmentModel::class.java)
        Log.d(TAG, "onCreate: ${objectArray!!.size}")
    }

    fun openCamera(
        ivPreview: ImageView,
        layoutPosition: Int,
        map: HashMap<Int, Bitmap>
    ) {

        this.map = map
        selectedImage = ivPreview
        selectedPostion = layoutPosition

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE
            )
        } else {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap?
            selectedImage.setImageBitmap(bitmap)
            map.put(selectedPostion!!, bitmap!!)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}