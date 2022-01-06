package com.example.multishoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.example.multishoppinglist.LoginActivity
import com.example.multishoppinglist.R
import com.example.multishoppinglist.User
import com.example.multishoppinglist.databinding.FragmentOfflineBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.lang.ref.Reference

class OfflineFragment : Fragment() {
    private lateinit var database: DatabaseReference

    private lateinit var binding: FragmentOfflineBinding

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
        return binding.root
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