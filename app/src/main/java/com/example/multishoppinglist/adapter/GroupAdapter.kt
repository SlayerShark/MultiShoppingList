package com.example.multishoppinglist.adapter

import android.content.Context
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
        holder.itemPrice.text = currentGroupItem.item_price

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

    }

}
