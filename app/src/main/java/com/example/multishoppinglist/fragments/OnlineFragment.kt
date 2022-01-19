package com.example.multishoppinglist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.multishoppinglist.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.multishoppinglist.databinding.FragmentOnlineBinding
import com.google.firebase.database.DatabaseReference

class OnlineFragment : Fragment() {
    private lateinit var binding: FragmentOnlineBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Shopping List - Online")

        //go to another fragment from fragment
        val fr = fragmentManager?.beginTransaction()
        fr?.replace(R.id.onlineMainFragment, GroupListFragment())?.addToBackStack("logfrag")
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
