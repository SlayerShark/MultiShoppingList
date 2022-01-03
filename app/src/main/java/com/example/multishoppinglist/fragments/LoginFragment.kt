package com.example.multishoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.multishoppinglist.MainActivity
import com.example.multishoppinglist.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

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
            var umail = email.text.toString()
            var pass = password.text.toString()

            auth.signInWithEmailAndPassword(umail, pass)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context,"Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }







        return view
    }
}
