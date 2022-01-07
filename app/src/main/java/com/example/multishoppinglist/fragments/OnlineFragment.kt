package com.example.multishoppinglist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.multishoppinglist.model.Group
import com.example.multishoppinglist.databinding.DialogAddGroupBinding
import com.example.multishoppinglist.databinding.FragmentOnlineBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class OnlineFragment : Fragment() {
    private lateinit var binding: FragmentOnlineBinding
    private lateinit var database: DatabaseReference

    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Shopping List - Online")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnlineBinding.inflate(inflater, container, false)

        binding.createGroupDialog.setOnClickListener { createGroup() }

        return binding.root
    }

    private fun createGroup() {
        val inflater = layoutInflater
        val binding: DialogAddGroupBinding = DialogAddGroupBinding.inflate(inflater)

        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Create New Group")
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(false)

        alertDialog.setNegativeButton("Cancel"){ dialog, which ->
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        }
        alertDialog.setPositiveButton("Create"){ dialog, which ->
            val groupName = binding.addGroupTitle.text.toString()

            database = FirebaseDatabase.getInstance().getReference("Group")
            val id = database.push().key        //to auto generate id
            val createdBy = auth.currentUser?.email
            val group = Group(id, groupName, createdBy)
            database.child(id!!).setValue(group)

            Toast.makeText(context, "Group Name: $groupName", Toast.LENGTH_LONG).show()
        }

        val dialog = alertDialog.create()
        dialog.show()

    }
}