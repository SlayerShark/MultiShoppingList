package com.example.multishoppinglist.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.multishoppinglist.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

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
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.textRegister.setOnClickListener {
            //go to another fragment from fragment
            val fr = fragmentManager?.beginTransaction()
            fr?.replace(R.id.loginMainFragment, registerFragment)?.addToBackStack("logfrag")
            fr?.commit()
        }

        //didn't worked on textView. so, button
        binding.noUserLogin.setOnClickListener {
            activity?.let {
                val intent = Intent(it, MainActivity::class.java)
                it.startActivity(intent)
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
//                        ProgressDialog(context).setMessage("Registering User")
                        val user = auth.currentUser
                        Toast.makeText(context, "Logged In Successfully", Toast.LENGTH_SHORT).show()
                        activity?.let {
                            val intent = Intent(it, MainActivity::class.java)
                            it.startActivity(intent)
                        }
//                        ProgressDialog(context).dismiss()
                    } else {
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        return binding.root
    }
}