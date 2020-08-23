package com.hacktech19

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class product_image : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"


    var isChanged: Boolean? = false
    private var mainImageUri: Uri? = null
    internal lateinit var myDB: FirebaseFirestore
    var compressedUserImage: Bitmap? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val intent=intent
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)
        myDB = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        view_image.setOnClickListener {
            /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this@image_activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this@image_activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                   // Snackbar.make(findViewById(R.id.setup_layout), "Please Grant permissions", Snackbar.LENGTH_SHORT).show()
                    ActivityCompat.requestPermissions(this@image_activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), READCODE)
                    ActivityCompat.requestPermissions(this@image_activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITECODE)

                } else {
                    bringImagePicker()
                }

            } else {*/
            bringImagePicker()
        }
        //}

        edit_image.setOnClickListener {
            if (isChanged!!) {
                Toast.makeText(this, "IMAGE UPLOADING PLEASE WAIT", Toast.LENGTH_SHORT).show()
                val newImageFile = File(mainImageUri!!.path!!)

                try {
                    compressedUserImage = Compressor(this@product_image)
                        .setQuality(60)
                        .compressToBitmap(newImageFile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val baos = ByteArrayOutputStream()
                compressedUserImage!!.compress(Bitmap.CompressFormat.JPEG, 60, baos)
                val imageData = baos.toByteArray()

                val image_path =
                    storageReference!!.child("imgProducts").child(sPref.getString("UserID","") + "product"+intent.getIntExtra("imgNo",1).toString() + ".jpg")
                image_path.putBytes(imageData).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Update Data on server Firestore database
                        Log.d("#999","before firebase image uri")
                        image_path?.downloadUrl?.addOnSuccessListener(this@product_image) { uri ->
                            myDB.collection("ItemInfo").document(intent.getStringExtra("RID").toString()).update(
                                "image"+intent.getIntExtra("count",1).toString(),
                                uri.toString()

                            ).addOnCompleteListener { task ->
                                Log.d("#999","before condition to dashboard")
                                if (task.isSuccessful) {
                                    Log.d("#999","condition to dashboard")
                                    Toast.makeText(this, "IMAGE UPLOADED SUCCESSFULY", Toast.LENGTH_SHORT).show()
                                    val i = Intent(this@product_image, activity_addnewitem::class.java)
                                    startActivity(i)
                                    Log.d("#999","intent to dashboard")

                                    //  Log.i("CHECK",intentThatStartedThisActivity);
                                    // sendToMain(intentThatStartedThisActivity);

                                }


                            }
                            // else {
                            //  Snackbar.make(findViewById(R.id.setup_layout), task.exception!!.message, Snackbar.LENGTH_SHORT).show()
                            // setupProgress.setVisibility(View.INVISIBLE)

                            //  }
                        }

                    }
                }

            }
        }
    }
            public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {

                        mainImageUri = result.uri
                        view_image.setImageURI(mainImageUri)
                        isChanged = true

                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        val error = result.error
                        //    Snackbar.make(findViewById(R.id.setup_layout),error.toString(),Snackbar.LENGTH_SHORT).show();

                    }
                }
            }

            private fun bringImagePicker() {
                CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this@product_image)
            }

            companion object {
            private val READCODE = 1
            private val WRITECODE = 2
        }



    }

