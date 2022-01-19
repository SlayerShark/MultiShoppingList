package com.example.multishoppinglist.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.multishoppinglist.R
import com.example.multishoppinglist.databinding.GroupListBinding
import com.example.multishoppinglist.fragments.GroupFragment
import com.example.multishoppinglist.model.Group
import com.google.firebase.database.*

class GroupListAdapter(private val groupList : ArrayList<Group>) : RecyclerView.Adapter<GroupListAdapter.groupListViewHolder>() {

    private lateinit var binding: GroupListBinding
    private lateinit var database: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): groupListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = GroupListBinding.inflate(inflater)
        return groupListViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: groupListViewHolder, position: Int) {
        val groupItem = groupList[position]

        holder.groupName.text = groupItem.group_name

        holder.itemView.setOnClickListener{
            Toast.makeText(it.context, "fragment: ${groupItem.group_name}", Toast.LENGTH_SHORT).show()

            //to send data from GroupListFragment to GroupFragment
            val bundle = Bundle()
            bundle.putString("grp_name", groupItem.group_name)
            val fragment = GroupFragment()
            fragment.arguments = bundle

            //go to another fragment from fragment
            val activity = it.getContext() as AppCompatActivity
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.onlineMainFragment, fragment)
                .addToBackStack(null)
                .commit()

        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    class groupListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupName : TextView = itemView.findViewById(R.id.textGroupName)
    }

}