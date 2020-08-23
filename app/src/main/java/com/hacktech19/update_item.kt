package com.hacktech19

import androidx.appcompat.app.AppCompatActivity

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast

import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.HashMap
import java.util.concurrent.TimeUnit

class update_item : AppCompatActivity(){
private var PRIVATE_MODE=0
private val PREF_NAME="PrefExpiry"


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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edititem)
        var sPref: SharedPreferences =getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        var mydb: FirebaseFirestore
        mydb = FirebaseFirestore.getInstance()

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

            picker = DatePickerDialog(this@update_item,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> editpublishdate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, year, month, day)
            picker.datePicker.minDate = Date().time //current date
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
                Toast.makeText(this@update_item, "Cannot set", Toast.LENGTH_SHORT).show()
            }

            picker = DatePickerDialog(this@update_item,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> editexpirydate.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, year, month, day)
            picker.datePicker.minDate = maxDate.time
            picker.show()
        }

        spin.setSelection(intent.getIntExtra("type",0))
        title.setText(intent.getStringExtra("title"))
        shortdesc.setText(intent.getStringExtra("shortdesc"))
        longdesc.setText(intent.getStringExtra("longdesc"))
        editpublishdate.setText(intent.getStringExtra("publishdate"))
        editexpirydate.setText(intent.getStringExtra("expirydate"))

        //intent.putExtra("type", user_id)



        btn.setOnClickListener {
            if (checkDataEntered()){
                Toast.makeText(this, "ITEM UPDATING PLEASE WAIT", Toast.LENGTH_SHORT).show()
                mydb.collection("ItemInfo").document(intent.getStringExtra("RID").toString()).update(
                    "type",
                    spin.selectedItem.toString(),
                    "shortdesc",
                    shortdesc.text!!.toString(),
                    "longdesc",
                    longdesc.text!!.toString(),
                    "title",
                    title.text!!.toString(),
                    "publishdate",
                    editpublishdate.text!!.toString(),
                    "expirydate",
                    editexpirydate.text!!.toString()

                )
                        .addOnSuccessListener {
                            val doc = myDB.collection("ItemInfo").document(intent.getStringExtra("RID").toString())
                            doc.get().addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "ITEM UPDATED", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@update_item, home::class.java))
                                    finish()
                                }
                            }
                        }.addOnFailureListener { Toast.makeText(this@update_item, "Cannot", Toast.LENGTH_SHORT).show() }
            } else {
                Toast.makeText(this@update_item, "Data Not Entered", Toast.LENGTH_SHORT).show()
            }
        }

    }



    internal fun itemspinner() {
        val items = arrayOf("Books", "Medicine", "Clothes", "Home")
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
        if (isEmpty(longdesc)) {
            longdesc.error = "Pls enter the description."
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
