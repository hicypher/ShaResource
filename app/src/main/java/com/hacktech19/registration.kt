package com.hacktech19
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import com.hacktech19.ConnectionDetector
import com.hacktech19.R
import kotlinx.android.synthetic.main.activity_snippest_reg.*
//import kotlinx.android.synthetic.main.snippet_reg.*
//import kotlinx.android.synthetic.main.snippet_reg.reg
import java.text.SimpleDateFormat
import java.util.*

class registration : AppCompatActivity() {

    private var PRIVATE_MODE=0
    private val PREF_NAME="PrefExpiry"
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {


        val sPref:SharedPreferences=getSharedPreferences(PREF_NAME,PRIVATE_MODE)


        var mydb: FirebaseFirestore
      mydb = FirebaseFirestore.getInstance()
        var format=SimpleDateFormat("dd MMM,YYYY",Locale.US)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)
        var check:Int=0
        val b = findViewById(R.id.pickdateDtn) as ImageButton
        Log.d("#009","On create  Calender")
        b.setOnClickListener(){

            Log.d("#009","Start Program Calender")
            val now= Calendar.getInstance()
            val datepicker=DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
                val selDate=Calendar.getInstance()
                selDate.set(Calendar.YEAR,year)
                selDate.set(Calendar.MONTH,month)
                selDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)

                var date=format.format(selDate.time) as String
                var date_String : EditText

               dateTv.setText(date.toString())
                 Toast.makeText(this,date,Toast.LENGTH_SHORT)
            },now.get(Calendar.YEAR)
            ,now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
            datepicker.show()
        }




            var gender=""
        reg.setOnClickListener {

            var cd= ConnectionDetector(this)
            var isInternet= false
            isInternet= cd.isConnectingToInternet
            if(rd_male.isChecked) gender="MALE"
            else gender="FEMALE"

            if(!android.util.Patterns.EMAIL_ADDRESS.matcher(et_user_name.text.toString()).matches() && !android.util.Patterns.PHONE.matcher(et_user_name.text.toString()).matches() )
          et_user_name.setError("Enter correct Email/Phone")
        else if(et_password.text.toString().length<6)
          et_password.setError("password must be atleast 6 digit long")
            else  if(et_password.text.toString()!=et_password_confirm.text.toString())
          et_password_confirm.setError("passwords does not match")
            else if(et_first_name.text.toString().trim().equals(""))
          et_first_name.setError("Enter first name")
            else if(et_last_name.text.toString().trim()=="")
          et_last_name.setError("Enter last name")
            else if(dateTv.text.toString()=="")
          dateTv.setError("select dob here")

            else if(gender.isEmpty())
          rd_female.setError("select gender")
            else if( !isInternet)
            Toast.makeText(this,"check your connection",Toast.LENGTH_SHORT).show()
            else {

             /*   val i=Intent(this,login::class.java)
                startActivity(i)*/
                val user = HashMap<String, Any>()
                user["UserID"] = et_user_name.text.toString()
                Log.d("#999","username ${et_user_name.text.toString()} ")
               // Log.d(TAG"#033, RNumber$strNumber")
                user["Password"] = et_password.text.toString()
               // Log.d(TAG, "#033 RId")
                user["fName"] = et_first_name.text.toString()
                user["lName"] = et_last_name.text.toString()
                user["dob"] = dateTv.text.toString()
                user["gender"] = gender
                user["image"] = ""
                user["publishdate"] = ""


                mydb.collection("Members").document(et_user_name.text.toString()).set(user)
                    .addOnSuccessListener {
                        Toast.makeText(this, "data inserted successfully", Toast.LENGTH_LONG).show()

                     //   Log.d(TAG, "#033 for blank Description")

                    }
                    .addOnFailureListener { e -> Log.w("#999", "Error adding document", e) }
                Toast.makeText(this,"SUCCESSFULLY REGISTERED\nwelcome to login page",Toast.LENGTH_SHORT).show()
                val intek=Intent(applicationContext, activity_addnewitem::class.java)
                sPref.edit().putString("UserID",et_user_name.text.toString()).apply()
                sPref.edit().putString("password",et_password.text.toString()).apply()
                sPref.edit().putString("fName",et_first_name.text.toString()).apply()
                sPref.edit().putString("lName",et_last_name.text.toString()).apply()
                sPref.edit().putString("dob",dateTv.text.toString()).apply()
                sPref.edit().putString("gender",gender.toString()).apply()
                startActivity(intek)
finish()
            }
        }
       /* if(private boolean isValidMobile(String phone) {
                return android.util.Patterns.PHONE.matcher(phone).matches();
            })*/


    }
}
