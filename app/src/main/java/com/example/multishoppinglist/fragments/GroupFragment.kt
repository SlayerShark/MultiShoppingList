package com.example.multishoppinglist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.multishoppinglist.databinding.DialogAddGroupBinding
import com.example.multishoppinglist.databinding.DialogAddItemBinding
import com.example.multishoppinglist.databinding.FragmentGroupBinding
import com.example.multishoppinglist.model.Group
import com.example.multishoppinglist.model.GroupItem
import com.example.multishoppinglist.model.Invite
import com.example.multishoppinglist.model.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class GroupFragment : Fragment() {
    private lateinit var binding: FragmentGroupBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGroupBinding.inflate(inflater, container, false)

        binding.addItemDialog.setOnClickListener { addItem() }
        binding.invite.setOnClickListener { invite() }

        return binding.root
    }

    private fun invite() {
        val inflater = layoutInflater
        val binding: DialogAddGroupBinding = DialogAddGroupBinding.inflate(inflater)

        binding.addGroupTitle.hint = "Email Address"
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Join Group")
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(false)

        alertDialog.setNegativeButton("Cancel") { dialog, which ->
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        }

        alertDialog.setPositiveButton("Invite") { dialog, which ->
            val arg = this.arguments
            val groupName = arg?.get("grp_name").toString()
            val inEmail = binding.addGroupTitle.text.toString()
            val invitedBy   = auth.currentUser?.email

            database = FirebaseDatabase.getInstance().getReference("Groups")
            database.addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data= inEmail.split(".")
                    val eml = data[0]

                    if (snapshot.child(eml).child(groupName!!).exists()) {
                        println("exists")
                        Toast.makeText(context, "User already in the Group", Toast.LENGTH_LONG).show()
                    }
                    else {
                        val invite = Invite(invitedBy, groupName, "basic")
                        database.child(eml).child(groupName!!).setValue(invite).addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "group created : $groupName",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        val dialog = alertDialog.create()
        dialog.show()
    }

    private fun addItem() {
        val inflater = layoutInflater
        val binding:DialogAddItemBinding = DialogAddItemBinding.inflate(inflater)

        val arg = this.arguments
        val group_name = arg?.get("grp_name").toString()
        println("group name: "+group_name)

        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setTitle("Add New Item")
        alertDialog.setView(binding.root)
        alertDialog.setCancelable(false)

        alertDialog.setNegativeButton("Cancel"){ dialog, which ->
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        }
        alertDialog.setPositiveButton("Add"){ dialog, which ->
            val itemName = binding.addTitle.text.toString()
            val itemDescription = binding.addDescription.text.toString()
            val itemQuantity = binding.addQuantity.text.toString()
            val userId = Firebase.auth.currentUser?.uid
            val groupName = group_name.toString()

            database = FirebaseDatabase.getInstance().getReference("GroupItems")
            val itemId = database.push().key        //to auto generate id
            val grpItem = GroupItem(itemId, userId, itemName, itemDescription, itemQuantity, groupName)
            database.child(itemId!!).setValue(grpItem).addOnSuccessListener {
                Toast.makeText(context, "Added Item: $itemName", Toast.LENGTH_LONG).show()
            }

        }

        alertDialog.create().show()

    }
}