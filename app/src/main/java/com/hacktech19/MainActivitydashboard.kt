package com.hacktech19

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_snippest_reg.*
//import kotlinx.android.synthetic.main.MainActivitydasboard.*
import kotlinx.android.synthetic.main.dashbord.*
//import kotlinx.android.synthetic.main.nav_header_main_navigation.*
import com.hacktech19.Model.ItemInfo
import com.squareup.picasso.Picasso

class MainActivitydashboard : AppCompatActivity() {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"

    private var storageReference: StorageReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashbord)
        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()
        var myDB = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        val tv_click_me = findViewById(R.id.et_item) as Button
        tv_click_me.setOnClickListener {
            // your code to run when the user clicks on the TextView

            val intent = Intent(this, MainActivity_edit_profile::class.java)

            startActivity(intent)
            finish()
        }


        val tv_click_me4 = findViewById(R.id.change) as Button
        tv_click_me4.setOnClickListener {
            // your code to run when the user clicks on the TextView


            val intent1 = Intent(this, image_activity::class.java)

            startActivity(intent1)
            finish()
        }




        var username=sPref.getString("UserID","")
        var password=sPref.getString("password","")
         var first_name=sPref.getString("fName","")
        var last_name=sPref.getString("lName","")
        var dob=sPref.getString("dob","")
        var gender=sPref.getString("gender","")
        u.setText("USERNAME : $username")

        p.setText("PASSWORD  : $password")
        f.setText("NAME  :  $first_name  $last_name"  )
        arvind.setText("DOB  :  $dob" )
                d.setText("GENDER  :  $gender")

        var imguri=""
        val docRef = mydb.collection("Members").document( sPref.getString("UserID","").toString())
        docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
            if (task.isSuccessful) {
                val document = task.result
                imguri=document!!.get("image").toString()
                if (document != null) {
                    Log.d("#999", "DocumentSnapshot data: " + document.data+imguri)
                    if(!imguri!!.isEmpty()) {
                        Picasso.get().load(imguri).into(imgview); Log.d("#999", "img document")}
                    else  Log.d("#999", "No such document")

                } else {
                    Log.d("#999", "No such document")
                }
            } else {
                Log.d("#999", "get failed with ", task.exception)
            }
        })

        if(!imguri!!.isEmpty()) {Picasso.get().load(imguri).into(imgview); Log.d("#999", "img document")}
        else  Log.d("#999", "No such document")


    }

}
