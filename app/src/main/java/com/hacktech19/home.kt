package com.hacktech19

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.content.Intent
import android.content.SharedPreferences
import com.hacktech19.Model.ItemInfo

//import com.hacktech19.RecyclerAdapters.RecyclerAdapterItemInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import om.hacktech19.RecyclerAdapters.RecyclerAdapterItemInfo
import com.hacktech19.R
import com.hacktech19.activity_edititem
import om.hacktech19.RecyclerAdapters.RecyclerAdapterItemInfoo

import java.util.ArrayList

class home : AppCompatActivity() {

    private var PRIVATE_MODE = 0
    private val PREF_NAME = "PrefExpiry"

    internal lateinit var btn: Button
    internal lateinit var mydB: FirebaseFirestore
    internal lateinit var recyclerView: RecyclerView
    internal lateinit var manager: RecyclerView.LayoutManager
    internal lateinit var datalist: ArrayList<ItemInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        btn = findViewById<View>(R.id.floating_btn) as Button
        recyclerView = findViewById<View>(R.id.item_recycler_view) as RecyclerView
        datalist = ArrayList()
        val sPref: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)

        mydB = FirebaseFirestore.getInstance()
        Log.d("#999", "Out")
            mydB.collection("ItemInfo").whereEqualTo("UserID", sPref.getString("UserID", "")).get()
                .addOnCompleteListener { task ->
                    Log.d("#999", "IN")
                    if (task.isSuccessful) {
                        Log.d("#999", "IN2")
                        for (doc in task.result!!) {
                            Log.d("Query", "Data Fetch")
                            val ulist = ItemInfo()
                            ulist.item_title = doc.data["title"]!!.toString()
                            ulist.item_shortdesc = doc.data["shortdesc"]!!.toString()
                            ulist.item_longdesc = doc.data["longdesc"]!!.toString()
                            ulist.item_type = doc.data["type"]!!.toString()
                            ulist.item_expirydate = doc.data["expirydate"]!!.toString()
                            ulist.item_publishdate = doc.data["publishdate"]!!.toString()
                            ulist.item_user_id = doc.id
                            ulist.image1 = doc.data["image0"]!!.toString()
                            ulist.image2 = doc.data["image1"]!!.toString()
                            ulist.image3 = doc.data["image2"]!!.toString()
                            ulist.RID = doc.data["RID"]!!.toString()
                            ulist.image_count = doc.data["image_count"]!!.toString()
                            datalist.add(ulist)

                        }
                        Log.d("#99", "Data set in class")
                        recyclerView.setHasFixedSize(true)
                        manager = LinearLayoutManager(this@home)
                        recyclerView.layoutManager = manager

                        val recyclerAdapterItemInfo = RecyclerAdapterItemInfoo(datalist, this@home)
                        recyclerView.adapter = recyclerAdapterItemInfo

                    }
                }
        btn.setOnClickListener {

            val i = Intent(this, activity_edititem::class.java)
            startActivity(i)
            finish()
        }
        }


    }

