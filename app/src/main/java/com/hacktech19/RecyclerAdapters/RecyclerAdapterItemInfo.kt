package om.hacktech19.RecyclerAdapters

import android.content.Context
import android.content.Intent
import com.hacktech19.Model.ItemInfo




import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage

import java.nio.charset.IllegalCharsetNameException

import android.app.Activity.RESULT_OK
import com.hacktech19.R
import com.hacktech19.activity_addnewitem
import com.hacktech19.product_image
import com.hacktech19.update_item

//import com.hacktech19.product_image
//import com.hacktech19.update_item
//import com.hacktech19.home
class RecyclerAdapterItemInfo(internal var datalist: List<ItemInfo>, internal var context: Context) : RecyclerView.Adapter<RecyclerAdapterItemInfo.View_Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): View_Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_data_list, parent, false)
        return View_Holder(view)
    }

    override fun onBindViewHolder(holder: View_Holder, position: Int) {
        val TITLE = datalist[position].item_title
        val ShortDesc = datalist[position].item_shortdesc
        val LongDesc = datalist[position].item_longdesc
        val ExpiryDate = datalist[position].item_expirydate
        val PublishDate = datalist[position].item_publishdate
        val Type = datalist[position].item_type
        val user_id = datalist[position].item_user_id
        val image1 = datalist[position].image1
        val image2 = datalist[position].image2
        val image3 = datalist[position].image3
        val RID=datalist[position].RID

        holder.title.text = TITLE
        holder.shortde.text = ShortDesc
        holder.longde.text = LongDesc
        holder.expirydate.text = ExpiryDate
        holder.publishdate.text = PublishDate
        holder.category.text = Type
       // Picasso.get().load(image1).into(holder.image1)
       // Picasso.get().load(image2).into(holder.image2)
       // Picasso.get().load(image3).into(holder.image3)
        holder.addimage.setOnClickListener {
         /*   val intent = Intent(context, product_image::class.java)

            val count = datalist[position].image_count
            intent.putExtra("UID", user_id)
            intent.putExtra("count", 1)
            context.startActivity(intent)*/

        }
       if (!image1!!.isEmpty())   Picasso.get().load(image1).into(holder.image1)
        if (!image2!!.isEmpty()) Picasso.get().load(image2).into(holder.image2)
        if (!image3!!.isEmpty()) Picasso.get().load(image3).into(holder.image3)

     /*   holder.image1.setOnClickListener {
            val intent = Intent(context, product_image::class.java)
            intent.putExtra("RID", RID)
            intent.putExtra("count", 0)
            context.startActivity(intent)

        }
        holder.image2.setOnClickListener {
            val intent = Intent(context, product_image::class.java)
            intent.putExtra("RID", RID)
            intent.putExtra("count", 1)
            context.startActivity(intent)

        }
        holder.image3.setOnClickListener {
            val intent = Intent(context, product_image::class.java)
            intent.putExtra("RID", RID)
            intent.putExtra("count", 2)
            context.startActivity(intent)


        }*/

        holder.editbtn.setOnClickListener {
           /* val intent = Intent(context, update_item::class.java)

            intent.putExtra("RID", RID)
            intent.putExtra("title", TITLE)
            intent.putExtra("shortdesc", ShortDesc)
            intent.putExtra("longdesc", LongDesc)
            intent.putExtra("expiry", ExpiryDate)
            intent.putExtra("publish", PublishDate)
            intent.putExtra("type", Type)
            context.startActivity(intent)*/
        }
        holder.deletebtn.setOnClickListener {
          /*  FirebaseFirestore.getInstance().collection("ItemInfo").document(user_id!!).delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, activity_addnewitem::class.java))
                    //updateUi(user_id);
                } else {
                    Toast.makeText(context, "Cannot delete", Toast.LENGTH_SHORT).show()
                }
            }*/
        }


    }


    internal fun updateUi(user: String) {
        FirebaseFirestore.getInstance().collection("ItemInfo").document(user).addSnapshotListener(EventListener { documentSnapshot, e ->
            if (e != null) {
                return@EventListener
            }

            if (documentSnapshot != null && documentSnapshot.exists()) {

            } else {
                Log.d("ONLINE", "Current data: null")
            }
        })
    }

    override fun getItemCount(): Int {
        return datalist.size
    }


    inner class View_Holder(view: View) : RecyclerView.ViewHolder(view) {

        var title: TextView
        var shortde: TextView
        var longde: TextView
        var expirydate: TextView
        var publishdate: TextView
        var category: TextView
        internal var editbtn: ImageView
        internal var deletebtn: ImageView
        internal var addimage: ImageView
        internal var image1: ImageView
        internal var image2: ImageView
        internal var image3: ImageView

        init {
            this.title = view.findViewById<View>(R.id.item_data_list_title) as TextView
            this.shortde = view.findViewById<View>(R.id.item_data_list_title_shortdesc) as TextView
            this.longde = view.findViewById<View>(R.id.item_data_list_title_longdesc) as TextView
            this.expirydate = view.findViewById<View>(R.id.item_data_list_expirydate) as TextView
            this.publishdate = view.findViewById<View>(R.id.item_data_list_publishdate) as TextView
            this.category = view.findViewById<View>(R.id.item_data_list_category) as TextView
            this.editbtn = view.findViewById<View>(R.id.item_data_list_edit) as ImageView
            this.deletebtn = view.findViewById<View>(R.id.item_data_list_delete) as ImageView
            this.addimage = view.findViewById<View>(R.id.item_data_list_addimage) as ImageView
            this.image1 = view.findViewById<View>(R.id.item_data_list_image_1) as ImageView
            this.image2 = view.findViewById<View>(R.id.item_data_list_image_2) as ImageView
            this.image3 = view.findViewById<View>(R.id.item_data_list_image_3) as ImageView


        }
    }
}
