package com.example.multishoppinglist.fragments

import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multishoppinglist.R
import com.example.multishoppinglist.adapter.GroupAdapter
import com.example.multishoppinglist.adapter.ShAdapter
import com.example.multishoppinglist.databinding.DialogAddGroupBinding
import com.example.multishoppinglist.databinding.FragmentNoGroupBinding
import com.example.multishoppinglist.databinding.FragmentOnlineBinding
import com.example.multishoppinglist.model.Group
import com.example.multishoppinglist.model.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.nio.file.Files.exists
import java.util.*
import kotlin.collections.ArrayList

class NoGroupFragment : Fragment() {
    private lateinit var binding: FragmentNoGroupBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth;

    private lateinit var recyclerView: RecyclerView
    private lateinit var groupArrayList: ArrayList<Group>
    private lateinit var adapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Shopping List - Online")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoGroupBinding.inflate(inflater, container, false)

        binding.createGroupDialog.setOnClickListener { createGroup() }
        binding.joinGroupDialog.setOnClickListener { joinGroup() }

        recyclerView = binding.groupRecycler
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        groupArrayList = arrayListOf<Group>()
        getGroupData()

        return binding.root
    }

    private fun getGroupData() {
        val email   = auth.currentUser?.email
        val data= email?.split(".")
        val eml = data?.get(0).toString()

        database = FirebaseDatabase.getInstance().getReference("Groups").child(eml)
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    groupArrayList.clear()   //to clear recyclerView before adding item to prevent double data in the recyclerView

                    for (groupSnapShot in snapshot.children){
                        val groupItem = groupSnapShot.getValue(Group::class.java)

                        groupArrayList.add(groupItem!!)
                    }
                    recyclerView.adapter = GroupAdapter(groupArrayList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    private fun createGroup() {
        val inflater = layoutInflater
        val binding: DialogAddGroupBinding = DialogAddGroupBinding.inflate(inflater)

        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Create New Group")
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(false)

        alertDialog.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        }
        alertDialog.setPositiveButton("Create") { dialog, which ->
            val groupName = binding.addGroupTitle.text.toString()

            database = FirebaseDatabase.getInstance().getReference("Groups")

            //check if the groupName is already in the database
            database.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val groupItem = snapshot.getValue(Group::class.java)
                    val created_By = groupItem?.created_by.toString()

                    val email   = auth.currentUser?.email
                    val data= email?.split(".")
                    val eml = data?.get(0)

                    if (snapshot.child(eml.toString()).child(groupName!!).exists()) {
                        println("exists")
                        Toast.makeText(context, "group already exists", Toast.LENGTH_LONG).show()
                    }
                    else{
                        println("created")
                        val id      = database.push().key        //to auto generate id

                        val group = Group(email,groupName, "admin")
//
                        database.child(eml.toString()).child(groupName!!).setValue(group).addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "group created : $groupName",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



        }

        val dialog = alertDialog.create()
        dialog.show()

    }

    private fun joinGroup() {
        val inflater = layoutInflater
        val binding: DialogAddGroupBinding = DialogAddGroupBinding.inflate(inflater)

        binding.addGroupTitle.hint = "Invite Code"
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Join Group")
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(false)

        alertDialog.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        }
        alertDialog.setPositiveButton("Join") { dialog, which ->
            val groupName = binding.addGroupTitle.text.toString()

//            database = FirebaseDatabase.getInstance().getReference("Group")
//            val id = database.push().key        //to auto generate id
//            val createdBy = auth.currentUser?.email
//            val group = Group(id, groupName, createdBy)
//            database.child(id!!).setValue(group)

            Toast.makeText(context, "Group Name: $groupName", Toast.LENGTH_LONG).show()
        }

        val dialog = alertDialog.create()
        dialog.show()
    }
}
