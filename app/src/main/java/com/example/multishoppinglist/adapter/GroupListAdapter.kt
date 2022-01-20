package com.example.multishoppinglist.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.multishoppinglist.R
import com.example.multishoppinglist.databinding.GroupListBinding
import com.example.multishoppinglist.fragments.GroupFragment
import com.example.multishoppinglist.model.Group
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

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

        holder.groupName.text = groupItem.group_name?.capitalize() ?: String()

        holder.itemView.setOnClickListener{
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

        holder.itemView.setOnLongClickListener(View.OnLongClickListener {
            val alertDialog = AlertDialog.Builder(it.context)
            alertDialog.setTitle("Delete ${groupItem.group_name} ?")
            alertDialog.setCancelable(false)

            alertDialog.setNegativeButton("No") { dialog, which ->
            }
            alertDialog.setPositiveButton("Yes") { dialog, which ->
                val email   = Firebase.auth.currentUser?.email
                val data= email?.split(".")
                val eml = data?.get(0).toString()

                database = FirebaseDatabase.getInstance().getReference("Groups").child(eml)
                database.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.child(groupItem.group_name.toString()).ref.removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }

            alertDialog.show()
            true
        })
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    class groupListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupName : TextView = itemView.findViewById(R.id.textGroupName)
    }

}