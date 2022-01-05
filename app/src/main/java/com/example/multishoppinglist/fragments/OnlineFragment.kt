package com.example.multishoppinglist.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.multishoppinglist.LoginActivity
import com.example.multishoppinglist.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.content.DialogInterface
import android.util.Log
import android.widget.TextView
import org.w3c.dom.Text

class OnlineFragment : Fragment() {
    private lateinit var auth: FirebaseAuth;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Shopping List - Online")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_online, container, false)

        val btn_create_group = view.findViewById<TextView>(R.id.createGroupDialog)
        btn_create_group.setOnClickListener { alertGroup() }

        return view
    }

    private fun alertGroup() {
        val inflater = layoutInflater

        val inflate_view = inflater.inflate(R.layout.dialog_add_group, null)

        val group_title = inflate_view.findViewById<EditText>(R.id.addGroupTitle)

        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Create New Group")
        alertDialog.setView(inflate_view)
        alertDialog.setCancelable(false)

        alertDialog.setNegativeButton("Cancel"){ dialog, which ->
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        }
        alertDialog.setPositiveButton("Create"){ dialog, which ->
            val groupTitle = group_title.text.toString()
            Toast.makeText(context, "Group Name: $group_title", Toast.LENGTH_LONG).show()
        }

        val dialog = alertDialog.create()
        dialog.show()

    }
}