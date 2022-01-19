package com.example.multishoppinglist.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.multishoppinglist.MainActivity
import com.example.multishoppinglist.model.User
import com.example.multishoppinglist.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var binding: FragmentRegisterBinding

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
            val email       = binding.regEmail.text.toString().trim()
            val password    = binding.regPassword.text.toString().trim()
            val name        = binding.regName.text.toString().trim()
            val country     = binding.regCountry.text.toString().trim()
            val phone       = binding.regPhone.text.toString().trim()

            val progDial = ProgressDialog(context)
            progDial.setMessage("Registering User...")
            progDial.setCancelable(false)
            progDial.setCanceledOnTouchOutside(false)
            progDial.show()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        //to store user all data
                        database = FirebaseDatabase.getInstance().getReference("Users")
                        val id = auth.currentUser?.uid
                        val user = User(id, name, email, country, phone)
                        if (id != null) {
                            progDial.setMessage("Logging In...")
                            progDial.setCancelable(false)
                            progDial.setCanceledOnTouchOutside(false)
                            progDial.show()

                            database.child(id).setValue(user).addOnSuccessListener {
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(requireActivity()) { task ->
                                        if (task.isSuccessful) {
                                            progDial.dismiss()

                                            auth.currentUser
                                            Toast.makeText(context, "User Registered and Logged In Successfully", Toast.LENGTH_SHORT).show()
                                            activity?.let {
                                                val intent = Intent(it, MainActivity::class.java)
                                                it.startActivity(intent)
                                            }
                                        } else {
                                            Toast.makeText(context, "User Registration Failed", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                            }
                        }
                        //clear text fields
                        binding.regEmail.text.clear()
                        binding.regPassword.text.clear()
                        binding.regName.text.clear()
                        binding.regCountry.text.clear()
                        binding.regPhone.text.clear()
                    }
                }
            }
        return binding.root
    }
}