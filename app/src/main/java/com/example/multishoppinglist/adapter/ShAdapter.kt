package com.example.multishoppinglist.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.multishoppinglist.R
import com.example.multishoppinglist.databinding.ShItemBinding
import com.example.multishoppinglist.model.Group
import com.example.multishoppinglist.model.Item
import com.google.firebase.database.*

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

        holder.itemName.text = currentItem.item_name
        holder.itemDescription.text = currentItem.item_description
        holder.itemQuantity.text = currentItem.item_quantity

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
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class  MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName : TextView = itemView.findViewById(R.id.itemName)
        val itemDescription : TextView = itemView.findViewById(R.id.itemDescription)
        val itemQuantity : TextView = itemView.findViewById(R.id.itemQuantity)

        val option : ImageView = itemView.findViewById(R.id.shOption)
    }

}
