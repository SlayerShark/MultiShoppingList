package com.example.multishoppinglist.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.multishoppinglist.R
import com.example.multishoppinglist.databinding.GroupListBinding
import com.example.multishoppinglist.model.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GroupAdapter(private val groupList : ArrayList<Group>) : RecyclerView.Adapter<GroupAdapter.groupViewHolder>() {

    private lateinit var binding: GroupListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): groupViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = GroupListBinding.inflate(inflater)
        return groupViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: groupViewHolder, position: Int) {
        val groupItem = groupList[position]

        holder.groupName.text = groupItem.group_name
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    class groupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupName : TextView = itemView.findViewById(R.id.textGroupName)
    }

}