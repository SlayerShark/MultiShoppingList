package com.example.multishoppinglist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.multishoppinglist.R
import com.example.multishoppinglist.databinding.DialogAddItemBinding
import com.example.multishoppinglist.databinding.ShItemBinding
import com.example.multishoppinglist.model.Group
import com.example.multishoppinglist.model.Item
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ShAdapter(private val itemList : ArrayList<Item>) : RecyclerView.Adapter<ShAdapter.MyViewHolder>() {

    private lateinit var binding: ShItemBinding
    private lateinit var database: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ShItemBinding.inflate(inflater)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.itemName.text        = currentItem.item_name?.capitalize() ?: toString()
        holder.itemDescription.text = currentItem.item_description
        holder.itemQuantity.text    = currentItem.item_quantity
        holder.itemPrice.text       = currentItem.item_price
        holder.itemCheckbox.isChecked     = currentItem.item_checked.toString().toBoolean()

        holder.option.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(it.context, holder.option)
            popupMenu.menuInflater.inflate(R.menu.option_item, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(p0: MenuItem?): Boolean {
                    when (p0!!.itemId) {
                        R.id.delItem -> {
                            database = FirebaseDatabase.getInstance().getReference("Items")
                            database.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    snapshot.child(currentItem.id.toString()).ref.removeValue()

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })
                            Toast.makeText(it.context,"delete clicked", Toast.LENGTH_SHORT).show()
                        }
                    }
                    return true
                }
            })
            popupMenu.show()
        }

        //for updating or viewing item
        holder.itemView.setOnClickListener {
            val inflater = LayoutInflater.from(it.context)
            val binding: DialogAddItemBinding = DialogAddItemBinding.inflate(inflater)
            binding.addTitle.setText(currentItem.item_name)
            binding.addQuantity.setText(currentItem.item_quantity)
            binding.addDescription.setText(currentItem.item_description)
            binding.addPrice.setText(currentItem.item_price)

            val alertDialog = AlertDialog.Builder(it.context)
            alertDialog.setTitle("View/Edit: ${currentItem.item_name}")
            alertDialog.setView(binding.root)

            alertDialog.setPositiveButton("Update"){ dialog, which ->
                val itemName        = binding.addTitle.text.toString()
                val itemDescription = binding.addDescription.text.toString()
                val itemQuantity    = binding.addQuantity.text.toString()
                val itemPrice       = binding.addPrice.text.toString()
                val id              = currentItem.id
                val userId = Firebase.auth.currentUser?.uid
                val itemChecked = currentItem.item_checked

                database = FirebaseDatabase.getInstance().getReference("Items")
                val item = Item(id, userId, itemName, itemDescription, itemQuantity, itemPrice, itemChecked)
                database.child(id!!).setValue(item)

                Toast.makeText(it.context, "Added Item: $itemName", Toast.LENGTH_LONG).show()
            }

            alertDialog.create().show()

        }

        //to set the item as purchased
        holder.itemCheckbox.setOnCheckedChangeListener { checkBox, _ ->
            val itemName = currentItem.item_name
            val itemDescription = currentItem.item_description
            val itemQuantity = currentItem.item_quantity
            val itemPrice = currentItem.item_price
            val id = currentItem.id
            val userId = Firebase.auth.currentUser?.uid

            database = FirebaseDatabase.getInstance().getReference("Items")

            if (checkBox.isChecked) {
                val item =
                    Item(id, userId, itemName, itemDescription, itemQuantity, itemPrice, true)
                database.child(id!!).setValue(item)
            }
            else if (!checkBox.isChecked) {
                val item =
                    Item(id, userId, itemName, itemDescription, itemQuantity, itemPrice, false)
                database.child(id!!).setValue(item)
            }
        }


    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName        : TextView  = itemView.findViewById(R.id.itemName)
        val itemDescription : TextView  = itemView.findViewById(R.id.itemDescription)
        val itemQuantity    : TextView  = itemView.findViewById(R.id.itemQuantity)
        val itemPrice       : TextView  = itemView.findViewById(R.id.itemPrice)

        val option : ImageView = itemView.findViewById(R.id.shOption)
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val itemCheckbox : CheckBox    = itemView.findViewById(R.id.itemCheckbox)
    }

}
