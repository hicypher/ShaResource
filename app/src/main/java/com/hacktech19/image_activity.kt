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
import com.hacktech19.MainActivitydashboard
import com.hacktech19.R
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.editphoto.*
import kotlinx.android.synthetic.main.editphoto.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException

class image_activity : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"


    var isChanged: Boolean? = false
    private var mainImageUri: Uri? = null
    internal lateinit var myDB: FirebaseFirestore
    var compressedUserImage: Bitmap? = null
    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.editphoto)
        myDB = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        edit.setOnClickListener {
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

        save.setOnClickListener {
            Toast.makeText(this, "IMAGE UPLOADING PLEASE WAIT", Toast.LENGTH_SHORT).show()
            if (isChanged!!) {

                val newImageFile = File(mainImageUri!!.path!!)

                try {
                    compressedUserImage = Compressor(this@image_activity)
                        .setQuality(60)
                        .compressToBitmap(newImageFile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                val baos = ByteArrayOutputStream()
                compressedUserImage!!.compress(Bitmap.CompressFormat.JPEG, 60, baos)
                val imageData = baos.toByteArray()

                val image_path =
                    storageReference!!.child("imgOwners").child(sPref.getString("UserID", "") + "address" + ".jpg")
                image_path.putBytes(imageData).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Update Data on server Firestore database
                        Log.d("#999","before firebase image uri")
                        image_path?.downloadUrl?.addOnSuccessListener(this@image_activity) { uri ->
                            myDB.collection("Members").document(sPref.getString("UserID","").toString()).update(
                                "image",
                                uri.toString()

                            ).addOnCompleteListener { task ->
                                Log.d("#999","before condition to dashboard")
                                if (task.isSuccessful) {
                                    Log.d("#999","condition to dashboard")
                                    val i = Intent(this@image_activity, MainActivitydashboard::class.java)
                                    startActivity(i)
                                    finish()
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
                        edit.setImageURI(mainImageUri)
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
                    .start(this@image_activity)
            }

            companion object {
            private val READCODE = 1
            private val WRITECODE = 2
        }



    }

