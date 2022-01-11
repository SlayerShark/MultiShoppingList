package com.example.multishoppinglist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multishoppinglist.R
import com.example.multishoppinglist.databinding.ShItemBinding
import com.example.multishoppinglist.model.Item

class ShAdapter(private val itemList : ArrayList<Item>) : RecyclerView.Adapter<ShAdapter.MyViewHolder>() {

    private lateinit var binding: ShItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ShItemBinding = ShItemBinding.inflate(inflater)
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.item_name.text = currentItem.item_name
        holder.item_description.text = currentItem.item_description
        holder.item_quantity.text = currentItem.item_quantity
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class  MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item_name : TextView = itemView.findViewById(R.id.itemName)
        val item_description : TextView = itemView.findViewById(R.id.itemDescription)
        val item_quantity : TextView = itemView.findViewById(R.id.itemQuantity)
    }

}