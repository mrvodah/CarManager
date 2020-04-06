package com.example.carmanager.view.ui.scan


import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import com.example.carmanager.BuildConfig

import com.example.carmanager.R
import com.example.carmanager.view.custom.SelectOptionDialogFragment
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_scan.*
import java.io.File
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ScanFragment : Fragment() {

    private val PERMISSION_CAM_REQUEST_CODE: Int = 1
    private val PERMISSION_GALLERY_REQUEST_CODE: Int = 2
    private val PICK_IMAGE_FROM_GALLERY: Int = 3
    private val TAKE_IMAGE_FROM_CAMERA: Int = 4

    private lateinit var mCurrentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClick()
    }

    private fun initClick() {
        btn_scan.setOnClickListener {
            val selectOptions = SelectOptionDialogFragment()
            selectOptions.setSelectOptionDialogFragmentListener(object :
                SelectOptionDialogFragment.SelectOptionDialogFragmentListener {
                override fun onCameraSelect() {
                    selectOptions.dismiss()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestCameraPermission()
                    } else {
                        openCamera()
                    }
                }

                override fun onGallerySelect() {
                    selectOptions.dismiss()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestGalleryPermission()
                    } else {
                        openGallery()
                    }
                }
            })
            selectOptions.show(activity!!.supportFragmentManager, "TAG")
        }

        btn_cancel.setOnClickListener {

        }

        btn_success.setOnClickListener {

        }
    }

    private fun requestGalleryPermission() {
        val permissions = ArrayList<String>()
        if (checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissions.size > 0) {
            val array = arrayOfNulls<String>(permissions.size)
            permissions.toArray(array)
            requestPermissions(array, PERMISSION_GALLERY_REQUEST_CODE)

        } else {
            openGallery()
        }
    }

    private fun requestCameraPermission() {
        val permissions = ArrayList<String>()
        if (checkSelfPermission(
                activity!!,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.CAMERA)
        }

        if (checkSelfPermission(
                activity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissions.size > 0) {
            val array = arrayOfNulls<String>(permissions.size)
            permissions.toArray(array)
            requestPermissions(array, PERMISSION_CAM_REQUEST_CODE)

        } else {
            openCamera()
        }
    }

    private fun openGallery() {
        val i = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(i, PICK_IMAGE_FROM_GALLERY)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val tempPhotoFile: File?
        tempPhotoFile = createImageFile()
        cameraIntent.putExtra(
            MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                activity!!,
                BuildConfig.APPLICATION_ID + ".provider",
                tempPhotoFile
            )
        )

        startActivityForResult(cameraIntent, TAKE_IMAGE_FROM_CAMERA)
    }

    private fun createImageFile(): File {
        val imageFileName = UUID.randomUUID().toString()
        val storageDir = Environment.getExternalStorageDirectory()

        val eduliveDir = File(storageDir, "carmanager")
        if (!eduliveDir.exists()) {
            eduliveDir.mkdir()
        }
        val image = File(eduliveDir, "$imageFileName.jpeg")
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var check = true
        if (requestCode == PERMISSION_CAM_REQUEST_CODE) {
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED)
                    check = false
            }

            if (check) {
                openCamera()
            }

        } else if (requestCode == PERMISSION_GALLERY_REQUEST_CODE) {
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED)
                    check = false
            }

            if (check) {
                openGallery()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            val selectedImage = data!!.data
            imageView.setImageURI(selectedImage)
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor?
            if (selectedImage != null) {
                cursor = activity!!.getContentResolver().query(
                    selectedImage,
                    filePathColumn, null, null, null
                )
                if (cursor != null) {
                    cursor.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor.getString(columnIndex)
                    cursor.close()
                    CropImage.activity(Uri.fromFile(File(picturePath)))
                        .start(activity!!, this)
                }
            }
        } else if (requestCode == TAKE_IMAGE_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            CropImage.activity(Uri.fromFile(File(mCurrentPhotoPath)))
                .start(activity!!, this)
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val file = File(resultUri.path!!)//create path from uri
                    val split = file.path.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()//split the path
                    val bitmap = decodeFile(split[0])

                    decodeTextFromBitmap(bitmap)
                } else {
                    val bitmap = decodeFile(getFilePath(activity!!, resultUri))

                    decodeTextFromBitmap(bitmap)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                error.printStackTrace()
            }
        }
    }

    private fun decodeTextFromBitmap(bitmap: Bitmap) {
        val textRecognizer = TextRecognizer.Builder(activity!!).build()

        if (!textRecognizer.isOperational) {
            Toast.makeText(activity!!, "Could not get the Text!", Toast.LENGTH_SHORT).show()
        } else {
            val frame = Frame.Builder().setBitmap(bitmap).build()

            val items = textRecognizer.detect(frame)

            val stringBuilder = StringBuilder()

            for (i in 0 until items.size()) {
                val textBlock = items.valueAt(i)
                var value = textBlock.value
                value = value.replace("\n", " - ")
                stringBuilder.append(value)
                Log.d("TAG", value)
            }

            tv_license_plate.text = stringBuilder.toString()
        }
    }

    private fun decodeFile(filePath: String?): Bitmap {

        // Decode image size
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, o)

        // The new size we want to scale to
        val REQUIRED_SIZE = 1280

        // Find the correct scale value. It should be the power of 2.
        var width_tmp = o.outWidth
        var height_tmp = o.outHeight
        var scale = 1
        while (width_tmp >= REQUIRED_SIZE || height_tmp >= REQUIRED_SIZE) {
            width_tmp /= 2
            height_tmp /= 2
            scale *= 2
        }

        // Decode with inSampleSize
        val o2 = BitmapFactory.Options()
        o2.inSampleSize = scale

        // Check if bitmap is rotated, if has return default
        return BitmapFactory.decodeFile(filePath, o2)
    }

    private fun getFilePath(context: Context, uri: Uri): String? {
        var uri = uri
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(
                context.applicationContext,
                uri
            )
        ) {
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                uri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("image" == type) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                selection = "_id=?"
                selectionArgs = arrayOf(split[1])
            }
        }
        if ("content".equals(uri.scheme!!, ignoreCase = true)) {

            if (isGooglePhotosUri(uri)) {
                return uri.lastPathSegment
            }

            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor?
            try {
                cursor = context.contentResolver
                    .query(uri, projection, selection, selectionArgs, null)
                val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (cursor.moveToFirst()) {
                    val value = cursor.getString(column_index)
                    cursor.close()
                    return value
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

}
