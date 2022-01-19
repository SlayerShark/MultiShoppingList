package com.example.multishoppinglist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multishoppinglist.R
import com.example.multishoppinglist.databinding.ShGroupItemBinding
import com.example.multishoppinglist.model.GroupItem

class GroupAdapter(private val groupItemList : ArrayList<GroupItem>): RecyclerView.Adapter<GroupAdapter.groupViewHolder>() {

    private lateinit var binding: ShGroupItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): groupViewHolder {
        binding = ShGroupItemBinding.inflate(LayoutInflater.from(parent.context))

        return groupViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: groupViewHolder, position: Int) {
        val currentGroupItem = groupItemList[position]

        holder.itemName.text        = currentGroupItem.item_name
        holder.itemDescription.text = currentGroupItem.item_description
        holder.itemQuantity.text    = currentGroupItem.item_quantity
    }

    override fun getItemCount(): Int {
        return groupItemList.size
    }

    class groupViewHolder(groupItemView : View):RecyclerView.ViewHolder(groupItemView) {
        val itemName : TextView = itemView.findViewById(R.id.itemName)
        val itemDescription : TextView = itemView.findViewById(R.id.itemDescription)
        val itemQuantity : TextView = itemView.findViewById(R.id.itemQuantity)

    }

}
