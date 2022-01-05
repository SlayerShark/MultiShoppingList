package com.example.multishoppinglist.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.multishoppinglist.R
import com.example.multishoppinglist.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.android.gms.tasks.OnCompleteListener

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    private val database = Firebase.database
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Register")

        binding.textLogin.setOnClickListener {
            fragmentManager?.popBackStack("logfrag", FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.regEmail.text.toString()
            val password = binding.regPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "successfully register", Toast.LENGTH_SHORT).show()
                        println("createUserWithEmail:success")
                        fragmentManager?.popBackStack("logfrag", FragmentManager.POP_BACK_STACK_INCLUSIVE)

                    } else {
                        Toast.makeText(requireContext(), task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        println("Authentication failed.")

                    }
                }
            }
        return binding.root
    }
}