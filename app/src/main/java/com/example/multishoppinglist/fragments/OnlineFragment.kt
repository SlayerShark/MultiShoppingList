package com.example.multishoppinglist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.multishoppinglist.R
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

/*
        database = FirebaseDatabase.getInstance().getReference("Group")
        database.child("id").get()
        Toast.makeText(context, "id: $id", Toast.LENGTH_LONG).show()
*/

        //go to another fragment from fragment
        val fr = fragmentManager?.beginTransaction()
        fr?.replace(R.id.onlineMainFragment, NoGroupFragment())?.addToBackStack("logfrag")
        fr?.commit()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnlineBinding.inflate(inflater, container, false)



        return binding.root
    }


}
