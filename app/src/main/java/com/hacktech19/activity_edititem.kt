package com.hacktech19

import androidx.appcompat.app.AppCompatActivity

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.android.synthetic.main.activity_snippest_reg.*
//import com.hacktech19.R

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import java.util.concurrent.TimeUnit

class activity_edititem : AppCompatActivity() {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"

    internal lateinit var editpublishdate: TextInputEditText
    internal lateinit var editexpirydate: TextInputEditText
    internal lateinit var title: TextInputEditText
    internal lateinit var shortdesc: TextInputEditText
    internal lateinit var longdesc: TextInputEditText
    internal lateinit var publishdate: ImageView
    internal lateinit var expirydate: ImageView
    internal lateinit var btn: Button
    internal lateinit var spin: Spinner
    internal lateinit var picker: DatePickerDialog
    internal lateinit var myDB: FirebaseFirestore
    internal lateinit var maxDate: Date
    internal var year_x: Int = 0
    internal var month_x: Int = 0
    internal var day_x: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edititem)
        title = findViewById<View>(R.id.item_title) as TextInputEditText
        shortdesc = findViewById<View>(R.id.item_shortdesc) as TextInputEditText
        longdesc = findViewById<View>(R.id.item_longdesc) as TextInputEditText
        publishdate = findViewById<View>(R.id.publish_date) as ImageView
        expirydate = findViewById<View>(R.id.expiry_date) as ImageView
        editpublishdate = findViewById<View>(R.id.edittext_publish_date) as TextInputEditText
        editexpirydate = findViewById<View>(R.id.edittext_expiry_date) as TextInputEditText
        myDB = FirebaseFirestore.getInstance()
        btn = findViewById<View>(R.id.submit_item) as Button
        itemspinner()


        //PROPERLY FORMAT DATE

        publishdate.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)

            picker = DatePickerDialog(this@activity_edititem,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> editpublishdate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, year, month, day)
            picker.datePicker.minDate = Date().time
            picker.show()
        }

        expirydate.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)
            try {
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                maxDate = dateFormatter.parse(editpublishdate.text!!.toString())
            } catch (e: ParseException) {
                Toast.makeText(this@activity_edititem, "Cannot set", Toast.LENGTH_SHORT).show()
            }

            picker = DatePickerDialog(this@activity_edititem,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> editexpirydate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, year, month, day)
            picker.datePicker.minDate = maxDate.time
            picker.show()
        }



        btn.setOnClickListener {
            if (checkDataEntered()) {
                Toast.makeText(this, "ITEM ADDING PLEASE WAIT", Toast.LENGTH_SHORT).show()
                val date = Date()
                val formatterDate = SimpleDateFormat("dd-MM-yyyy")
                var strDate = formatterDate.format(date)
                val formatterDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                var strDateTime = formatterDateTime.format(date)
                val formatDateTime = SimpleDateFormat("HH:mm:ss")
                var dateTime = formatDateTime.format(date)

                val inputString = sPref.getString("UserID","")
                var strPayID = inputString + strDate + dateTime
                strPayID = strPayID.replace("[^a-zA-Z0-9]".toRegex(), "")
                sPref.edit().putString("strpayid",strPayID).apply()
                val item_info = HashMap<String, Any>()
                item_info["type"] = spin.selectedItem.toString()
                item_info["shortdesc"] = shortdesc.text!!.toString()
                item_info["longdesc"] = longdesc.text!!.toString()
                item_info["title"] = title.text!!.toString()
                //ADD REAL DATE
                item_info["publishdate"] = editpublishdate.text!!.toString()
                item_info["expirydate"] = editexpirydate.text!!.toString()
                item_info["image0"] = ""
                item_info["image1"] = ""
                item_info["image2"] = ""
                item_info["image_count"] = "-1"
                item_info["RID"]=strPayID
                item_info["UserID"]=sPref.getString("UserID","")

                myDB.collection("ItemInfo").document(strPayID.toString()).set(item_info)
                        .addOnSuccessListener {
                            val doc = myDB.collection("ItemInfo").document(strPayID.toString())
                            doc.get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "ITEM ADDED", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@activity_edititem, home::class.java))
                                    finish()
                                }
                            }
                        }.addOnFailureListener { Toast.makeText(this@activity_edititem, "Cannot", Toast.LENGTH_SHORT).show() }
            } else {
                Toast.makeText(this@activity_edititem, "Data Not Entered", Toast.LENGTH_SHORT).show()
            }
        }
    }

    internal fun itemspinner() {
        val items = arrayOf("Books", "Medicine", "Clothes", "Assets","Stationaries","Electronics","Others")
        spin = findViewById<View>(R.id.edititemselect) as Spinner
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spin.adapter = adapter

    }

    internal fun isEmpty(editText: TextInputEditText): Boolean {
        val str = editText.text!!.toString()
        return TextUtils.isEmpty(str)
    }

    internal fun checkDataEntered(): Boolean {
        var istrue = true
        if (isEmpty(title)) {
            title.error = "Pls enter the title."
            istrue = false
        }
        if (isEmpty(shortdesc)) {
            shortdesc.error = "Pls enter the short description."
            istrue = false
        }
        if (!android.util.Patterns.PHONE.matcher(longdesc.text.toString()).matches()) {
            longdesc.error = "Pls enter the mobile no.."
            istrue = false
        }
        if (istrue) {
            val format = SimpleDateFormat("dd-MM-yyyy")
            val pub = editpublishdate.text!!.toString()
            val expirydate = editexpirydate.text!!.toString()
            var dSynDate: Date? = null
            var dToday: Date? = null
            try {
                dSynDate = format.parse(expirydate)
                dToday = format.parse(pub)
                val diff = dSynDate!!.time - dToday!!.time
                val NumberOfMiuts = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS).toInt()
                Log.d("#TIME", Integer.toString(NumberOfMiuts))
                if (NumberOfMiuts < 1)
                    istrue = false
            } catch (e: ParseException) {

            }

        }
        return istrue
    }

    companion object {
        private val ALERT_DIALOG = 0
    }

}
