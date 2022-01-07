package com.example.multishoppinglist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.multishoppinglist.databinding.DialogAddGroupBinding
import com.example.multishoppinglist.databinding.DialogAddItemBinding
import com.example.multishoppinglist.databinding.FragmentOfflineBinding
import com.example.multishoppinglist.model.Group
import com.example.multishoppinglist.model.Item
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class OfflineFragment : Fragment() {
    private lateinit var binding: FragmentOfflineBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentOfflineBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Shopping List = Local")

        val user = Firebase.auth.currentUser
        user?.let {
//            val email = user.email
            val id : String = user.uid
            readData(id)
        }

        binding.addItemDialog.setOnClickListener{ addItem() }

        return binding.root
    }

    private fun addItem() {
        val inflater = layoutInflater
        val binding: DialogAddItemBinding = DialogAddItemBinding.inflate(inflater)

        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Add New Item")
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(false)

        alertDialog.setNegativeButton("Cancel"){ dialog, which ->
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        }
        alertDialog.setPositiveButton("Add"){ dialog, which ->
            val itemName = binding.addTitle.text.toString()
            val itemQuantity = binding.addQuantity.text.toString()
            val id = database.push().key        //to auto generate id
            val userId = Firebase.auth.currentUser?.uid

            database = FirebaseDatabase.getInstance().getReference("Items")
            val item = Item(id, userId, itemName, itemQuantity)
            database.child(id!!).setValue(item)

            Toast.makeText(context, "Added Item: $itemName", Toast.LENGTH_LONG).show()
        }

        val dialog = alertDialog.create()
        dialog.show()

    }


    //this function for retrieving data from rtdb using Uid
    private fun readData(id : String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(id).get().addOnSuccessListener {
            val name = it.child("name").value
            binding.hello.setText("Welcome, "+name.toString())
        }


    }
}