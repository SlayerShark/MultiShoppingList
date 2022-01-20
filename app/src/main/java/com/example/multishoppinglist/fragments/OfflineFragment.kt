package com.example.multishoppinglist.fragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.multishoppinglist.adapter.ShAdapter
import com.example.multishoppinglist.databinding.DialogAddItemBinding
import com.example.multishoppinglist.databinding.FragmentOfflineBinding
import com.example.multishoppinglist.databinding.ShItemBinding
import com.example.multishoppinglist.model.Item
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class OfflineFragment : Fragment() {
    private lateinit var binding: FragmentOfflineBinding
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemArrayList: ArrayList<Item>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = Firebase.auth.currentUser
        user?.let {
            val id : String = user.uid
            readData(id)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentOfflineBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setTitle("Shopping List = Local")

        binding.addItemDialog.setOnClickListener{ addItem() }

        recyclerView = binding.offlineRecycler
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        itemArrayList = arrayListOf<Item>()

        getItemData()

        return binding.root
    }

    private fun getItemData() {
        val progDialog = ProgressDialog(context)
//        progDialog.setMessage("loading")
//        progDialog.setCanceledOnTouchOutside(false)
//        progDialog.setCancelable(false)
//        progDialog.show()

        database = FirebaseDatabase.getInstance().getReference("Items")
        database.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    itemArrayList.clear()   //to clear recyclerView before adding item to prevent double data in the recyclerView

                    for (itemSnapshot in snapshot.children){
                        val item = itemSnapshot.getValue(Item::class.java)
                        //condition to display only the logged in users' information
                        val userId = item?.user_id
                        if (userId == Firebase.auth.currentUser?.uid){
                            if (item != null) {
                                itemArrayList.add(item)
                            }
                        }
                    }

                    recyclerView.adapter = ShAdapter(itemArrayList)
                    progDialog.dismiss()

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addItem() {
        val inflater = layoutInflater
        val binding: DialogAddItemBinding = DialogAddItemBinding.inflate(inflater)

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
            val id = database.push().key        //to auto generate id
            val userId = Firebase.auth.currentUser?.uid

            database = FirebaseDatabase.getInstance().getReference("Items")
            val item = Item(id, userId, itemName, itemDescription, itemQuantity)
            database.child(id!!).setValue(item)

            Toast.makeText(context, "Added Item: $itemName", Toast.LENGTH_LONG).show()
        }

        alertDialog.create().show()
    }


    //this function for retrieving data from rtdb using Uid
    private fun readData(id : String) {
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child(id).get().addOnSuccessListener {
            val name = it.child("name").value
            binding.hello.setText("Welcome, "+name.toString())
        }
    }

}