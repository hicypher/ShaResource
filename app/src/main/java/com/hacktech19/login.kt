package com.hacktech19

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import android.widget.Toast.LENGTH_SHORT
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.hacktech19.Model.ItemInfo
import com.hacktech19.R
import com.hacktech19.activity_addnewitem
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dashbord.*

//import kotlinx.android.synthetic.main.loginpage.*

class login : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()
        var a = "asd"
        var b = ""
        var c = ""
        var d = ""
        var e = ""
        var f = ""
        var g = ""
        var h = "123"
        var un = findViewById(R.id.username) as EditText
        var pass = findViewById(R.id.password) as EditText

        l.setOnClickListener {
            mydb = FirebaseFirestore.getInstance()
            Log.d("#00", "firebase ")
            var cd = ConnectionDetector(this)
            var isInternet = false
            isInternet = cd.isConnectingToInternet
            if (isInternet) {
                if (username.text.toString().trim().equals(""))
                        un.setError("please enter username")
                else {
                    Toast.makeText(this, "DATA CHECKING PLEASE WAIT", LENGTH_SHORT).show()
                    val docRef = mydb.collection("Members").document(username.text.toString())
                    docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                        if (task.isSuccessful) {
                            val document = task.result
                            a = document!!.get("Password").toString()
                            h = document!!.get("UserID").toString()
                            b = document!!.get("fName").toString()

                            c = document!!.get("lName").toString()
                            d = document!!.get("dob").toString()
                            e = document!!.get("gender").toString()
                            f = document!!.get("image").toString()
                            g = document!!.get("publishdate").toString()
                             if (h != username.text.toString())
                                un.setError("invalid username")
                            else if (password.text.toString().trim().length == 0)
                                pass.setError("please enter password")
                            else if (password.text.toString() != a.toString())
                                pass.setError("passwords does not match")
                            else {
                                Toast.makeText(this, "DATA INSERTING PLEASE WAIT", LENGTH_SHORT).show()
                                mydb = FirebaseFirestore.getInstance()
                                Log.d("#00", "firebase ")


                                val docRef = mydb.collection("Members").document(username.text.toString())
                                docRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
                                    if (task.isSuccessful) {
                                        val document = task.result
                                        a = document!!.get("Password").toString()


                                    }
                                }
                                )

                                Toast.makeText(this, "LOGIN SUCCESSFULL", LENGTH_SHORT).show()

                                val intek = Intent(applicationContext, activity_addnewitem::class.java)
                                sPref.edit().putString("UserID", username.text.toString()).apply()
                                sPref.edit().putString("password", a.toString()).apply()
                                sPref.edit().putString("fName", b.toString()).apply()
                                sPref.edit().putString("lName", c.toString()).apply()
                                sPref.edit().putString("dob", d.toString()).apply()
                                sPref.edit().putString("gender", e.toString()).apply()
                                startActivity(intek)
                                finish()
                            }

                        }


                    }
                    )
                }
            }
            else {
                if (!isInternet)
                    Toast.makeText(this, "check your connection", Toast.LENGTH_SHORT).show()
            }


        }




        r.setOnClickListener {

            val i = Intent(this, registration::class.java)
            startActivity(i)
        }

    }

}