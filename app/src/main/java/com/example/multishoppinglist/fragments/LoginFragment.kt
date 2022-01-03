package com.example.multishoppinglist.fragments

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.print.PrinterId
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.multishoppinglist.LoginActivity
import com.example.multishoppinglist.MainActivity
import com.example.multishoppinglist.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    val database = Firebase.database
    private val registerFragment = RegisterFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Login")
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val register = view.findViewById<TextView>(R.id.text_register)
        register.setOnClickListener {
            //go to another fragment from fragment
            val fr = fragmentManager?.beginTransaction()
            fr?.replace(R.id.loginMainFragment, registerFragment)?.addToBackStack("logfrag")
            fr?.commit()
        }

        //didn't worked on textView. so, button
        val gotoMainActivity = view.findViewById<Button>(R.id.gotoMainActivity)
        gotoMainActivity.setOnClickListener {
            Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()

            activity?.let {
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
            }
        }

        val email = view.findViewById<EditText>(R.id.loginEmail)
        val password = view.findViewById<EditText>(R.id.loginPassword)

        val button = view.findViewById<Button>(R.id.btnLogin)
        button.setOnClickListener {
            auth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        println("sucess")
                    } else {
                        Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                        println("failed")
                    }
                }


        }






        return view
    }
}