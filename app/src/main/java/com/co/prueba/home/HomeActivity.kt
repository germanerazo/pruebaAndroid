package com.co.prueba.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.co.prueba.R
import com.co.prueba.contact.Contact
import com.co.prueba.contact.ContactActivity
import com.co.prueba.contact.ContactAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var contactList:MutableList<Contact>
    private lateinit var listContact: ListView
    private lateinit var edtSearch: EditText
    private lateinit var baseList: MutableList<Contact>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.toolbar))

        val actionBar = supportActionBar
        actionBar!!.title = "Contacts"


        contactList = mutableListOf()
        baseList = mutableListOf()
        listContact = findViewById(R.id.listContacts)
        edtSearch = findViewById(R.id.edtSearch)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("Contacts")

        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val userId = FirebaseAuth.getInstance().uid
                if (snapshot!!.exists()){
                    contactList.clear()
                    for(c in snapshot.children){
                        val contact = c.getValue(Contact::class.java)
                        if (contact != null) {
                            if(contact.userId == userId.toString()){
                                contactList.add(contact!!)
                            }
                        }
                    }
                    val adapter = ContactAdapter(this@HomeActivity, R.layout.contacts,contactList)
                    listContact.adapter = adapter
                }
            }

        })

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            startActivity(Intent(this, ContactActivity::class.java))
        }

        edtSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val edtSearch = edtSearch.text
                baseList.clear()
                for(c in contactList){
                    if (c.name.contains(edtSearch)) {
                        baseList.add(c)
                    }
                }

                val adapter = ContactAdapter(this@HomeActivity, R.layout.contacts,baseList)
                listContact.adapter = adapter
            }
        })
    }
}