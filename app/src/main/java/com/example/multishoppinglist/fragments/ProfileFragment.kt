package com.example.multishoppinglist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.multishoppinglist.R
import com.example.multishoppinglist.databinding.FragmentProfileBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Profile")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance().getReference("Users")
        val id = Firebase.auth.uid
        database.child(id!!).get().addOnSuccessListener {
            val name    = it.child("name").value
            val email   = it.child("email").value
            val phone   = it.child("phone").value

            binding.proName.setText(name.toString())
            binding.proEmail.setText(email.toString())
            binding.proPhone.setText(phone.toString())
        }
        return binding.root
    }
}