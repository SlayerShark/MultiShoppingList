package com.example.multishoppinglist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.multishoppinglist.R
import com.example.multishoppinglist.databinding.DialogAddItemBinding
import com.example.multishoppinglist.databinding.ShGroupItemBinding
import com.example.multishoppinglist.fragments.GroupFragment
import com.example.multishoppinglist.model.GroupItem
import com.google.firebase.database.*

class GroupAdapter(private val groupItemList : ArrayList<GroupItem>): RecyclerView.Adapter<GroupAdapter.groupViewHolder>() {

    private lateinit var binding: ShGroupItemBinding
    private lateinit var database: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): groupViewHolder {
        binding = ShGroupItemBinding.inflate(LayoutInflater.from(parent.context))

        return groupViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: groupViewHolder, position: Int) {
        val currentGroupItem = groupItemList[position]

        holder.itemName.text        = currentGroupItem.item_name?.capitalize() ?: String()
        holder.itemDescription.text = currentGroupItem.item_description
        holder.itemQuantity.text    = currentGroupItem.item_quantity
        holder.itemAddedBy.text     = currentGroupItem.user_name
        holder.itemPrice.text       = currentGroupItem.item_price
        holder.switch.isChecked     = currentGroupItem.item_checked.toString().toBoolean()

        holder.itemOption.setOnClickListener { task ->
            val popupMenu: PopupMenu = PopupMenu(task.context, holder.itemOption)
            popupMenu.menuInflater.inflate(R.menu.option_item, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(p0: MenuItem?): Boolean {
                    when (p0!!.itemId) {
                        R.id.delItem -> {
                            database = FirebaseDatabase.getInstance().getReference("GroupItems").child(currentGroupItem.group_name.toString())
                            database.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    snapshot.child(currentGroupItem.id.toString()).ref.removeValue()
                                        .addOnSuccessListener {
                                            Toast.makeText(task.context, "Item Deleted Successfully", Toast.LENGTH_SHORT).show()
                                        }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })
                        }
                    }
                    return true
                }
            })
            popupMenu.show()
        }

        //for updating or viewing the item
        holder.itemView.setOnClickListener { task ->
            val inflater = LayoutInflater.from(task.context)
            val binding: DialogAddItemBinding = DialogAddItemBinding.inflate(inflater)

            binding.addTitle.setText(currentGroupItem.item_name)
            binding.addQuantity.setText(currentGroupItem.item_quantity)
            binding.addDescription.setText(currentGroupItem.item_description)
            binding.addPrice.setText(currentGroupItem.item_price)

            val alertDialog = AlertDialog.Builder(task.context)
            alertDialog.setTitle("View/Edit: ${currentGroupItem.group_name}")
            alertDialog.setView(binding.root)

            alertDialog.setPositiveButton("Update"){ dialog, which ->
                val itemName        = binding.addTitle.text.toString()
                val itemDescription = binding.addDescription.text.toString()
                val itemQuantity    = binding.addQuantity.text.toString()
                val itemPrice       = binding.addPrice.text.toString()
                val itemId      = currentGroupItem.id
                val userName    = currentGroupItem.user_name
                val groupName   = currentGroupItem.group_name
                val itemChecked = currentGroupItem.item_checked

                database = FirebaseDatabase.getInstance().getReference("GroupItems")
                val grpItem = GroupItem(itemId, userName, itemName, itemDescription, itemQuantity, itemPrice, groupName, itemChecked)
                database.child(groupName!!).child(itemId!!).setValue(grpItem).addOnSuccessListener {
                    Toast.makeText(task.context, "Item Edited: $itemName", Toast.LENGTH_LONG).show()
                }

            }

            alertDialog.create().show()
        }

        //to change the job done status/state
        holder.switch.setOnCheckedChangeListener { switchButton, b ->
            val itemName        = currentGroupItem.item_name
            val itemDescription = currentGroupItem.item_description
            val itemQuantity    = currentGroupItem.item_quantity
            val itemPrice       = currentGroupItem.item_price
            val itemId          = currentGroupItem.id
            val userName        = currentGroupItem.user_name
            val groupName       = currentGroupItem.group_name

            database = FirebaseDatabase.getInstance().getReference("GroupItems")

            if (switchButton.isChecked){
                val grpItem = GroupItem(itemId, userName, itemName, itemDescription, itemQuantity, itemPrice, groupName, "true")
                database.child(groupName!!).child(itemId!!).setValue(grpItem).addOnSuccessListener {
                    Toast.makeText(switchButton.context, "Completed", Toast.LENGTH_SHORT).show()
                }
            }
            else if (!switchButton.isChecked){
                    val grpItem = GroupItem(itemId, userName, itemName, itemDescription, itemQuantity, itemPrice, groupName, "false")
                    database.child(groupName!!).child(itemId!!).setValue(grpItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return groupItemList.size
    }

    class groupViewHolder(groupItemView : View):RecyclerView.ViewHolder(groupItemView) {
        val itemName        : TextView = itemView.findViewById(R.id.itemName)
        val itemDescription : TextView = itemView.findViewById(R.id.itemDescription)
        val itemQuantity    : TextView = itemView.findViewById(R.id.itemQuantity)
        val itemAddedBy     : TextView = itemView.findViewById(R.id.itemAddedBy)
        val itemPrice       : TextView = itemView.findViewById(R.id.itemPrice)

        val itemOption : ImageView = itemView.findViewById(R.id.grpItemOption)

        val switch : Switch = itemView.findViewById(R.id.itemSwitch)

    }

}
