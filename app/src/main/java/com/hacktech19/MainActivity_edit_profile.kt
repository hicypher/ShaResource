package com.hacktech19

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_snippest_reg.*
import kotlinx.android.synthetic.main.dashbord.*
import kotlinx.android.synthetic.main.edit_profile.*
import java.util.*

class MainActivity_edit_profile : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"


    internal lateinit var cd: ConnectionDetector
    internal var isInternetPresent: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()
        var cd=ConnectionDetector(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile)

        val c= Calendar.getInstance()
        val year =c.get(Calendar.YEAR)
        val month =c.get(Calendar.MONTH)
        val day=c.get(Calendar.DAY_OF_MONTH)
        ickdateDtn.setOnClickListener {
            val dpd=DatePickerDialog(this,DatePickerDialog.OnDateSetListener{viewModelStore, mYear :Int ,mMonth :Int ,mDay :Int->

                dateTvc.setText(""+ mDay +"/"+ mMonth +"/"+ mYear)
            },year,month,day)

            dpd.show()
        }
        var usernamea=sPref.getString("UserID","")
        var passworde=sPref.getString("password","")
        var first_namee=sPref.getString("fName","")
        var last_namee=sPref.getString("lName","")
        var dobe=sPref.getString("dob","")
        var conform_pass=sPref.getString("password","")
        user_name.setText(" $usernamea")
        passworda.setText("$passworde")
        first_namea.setText("$first_namee")
        last_namea.setText("$last_namee")
        dateTvc.setText("$dobe")
        password_confirm.setText("$passworde")
        var regaa= findViewById<ImageView>(R.id.imagec)
        regaa.setOnClickListener {
            val i = Intent(this, image_activity::class.java)
            startActivity(i)
        }



        var rega= findViewById<Button>(R.id.cvbn)
        rega.setOnClickListener {
            isInternetPresent = cd.isConnectingToInternet

            if (passworda.text.toString().length < 6)
                Toast.makeText(this, "enter atleast 6 digit password", Toast.LENGTH_SHORT).show()
            else if (passworda.text.toString() != password_confirm.text.toString())
                Toast.makeText(this, "passwords does not match", Toast.LENGTH_SHORT).show()
            else if (first_namea.text.toString().trim().equals(""))
                Toast.makeText(this, "enter first name ", Toast.LENGTH_SHORT).show()
            else if (last_namea.text.toString().trim() == "")
                Toast.makeText(this, "enter last name", Toast.LENGTH_SHORT).show()
            else if (dateTvc.text.toString() == "")
                Toast.makeText(this, "Enter DOB correctly", Toast.LENGTH_SHORT).show()
            else if(gender.checkedRadioButtonId==0)
                Toast.makeText(this, "select gender", Toast.LENGTH_SHORT).show()
            else if(!isInternetPresent!!)
                Toast.makeText(this, "internet not present ", Toast.LENGTH_SHORT).show()
            else {

                val user = HashMap<String, Any>()
                user["UserID"] = usernamea
                Log.d("#999","username {usernamea} ")
                // Log.d(TAG"#033, RNumber$strNumber")
                user["Password"] = passworda.text.toString()
                // Log.d(TAG, "#033 RId")
                user["fName"] = first_namea.text.toString()
                user["lName"] = last_namea.text.toString()
                user["dob"] = dateTvc.text.toString()

                mydb.collection("Members").document(usernamea).set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "data inserted successfully", Toast.LENGTH_LONG).show()

                        //   Log.d(TAG, "#033 for blank Description")


                    }
                    .addOnFailureListener { e -> Log.w("#999", "Error adding document", e) }
                val editor = sPref.edit()
                editor.putString("UserID",usernamea)
                editor.apply()
                val editor1 = sPref.edit()
                editor1.putString("password", passworda.text.toString())
                editor1.apply()
                val editor2 = sPref.edit()
                editor2.putString("fName", first_namea.text.toString())
                editor2.apply()
                val editor3 = sPref.edit()
                editor3.putString("lName", last_namea.text.toString())
                editor3.apply()
                val editor4 = sPref.edit()
                editor4.putString("dob", dateTvc.text.toString())
                editor4.apply()


                val i = Intent(this, MainActivitydashboard::class.java)
                startActivity(i)
                Toast.makeText(this, "SUCCESSFULLY REGISTERED\nwelcome to ExpiryMatch", Toast.LENGTH_SHORT).show()
                finish()
            }
        }


        var imguri=""
        val docRef = mydb.collection("Members").document( sPref.getString("UserID","").toString())
        docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
            if (task.isSuccessful) {
                val document = task.result
                imguri=document!!.get("image").toString()
                if (document != null) {
                    Log.d("#999", "DocumentSnapshot data: " + document.data+imguri)
                    if(!imguri!!.isEmpty()) {
                        Picasso.get().load(imguri).into(imagec); Log.d("#999", "img document")}
                    else  Log.d("#999", "No such document")

                } else {
                    Log.d("#999", "No such document")
                }
            } else {
                Log.d("#999", "get failed with ", task.exception)
            }
        })

        if(!imguri!!.isEmpty()) {
            Picasso.get().load(imguri).into(imgview); Log.d("#999", "img document")}
        else  Log.d("#999", "No such document")




    }
}


