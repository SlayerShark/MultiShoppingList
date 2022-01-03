package com.example.multishoppinglist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.multishoppinglist.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterFragment : Fragment() {
    private val database = Firebase.database
    private val myRef = database.getReference("message")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myRef.setValue("Hello, World!")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Register")

        val login = view.findViewById<TextView>(R.id.text_login)
        login.setOnClickListener {
            fragmentManager?.popBackStack("logfrag", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        val register = view.findViewById<Button>(R.id.btnRegister)
        register.setOnClickListener{
        }

        return view
    }
}