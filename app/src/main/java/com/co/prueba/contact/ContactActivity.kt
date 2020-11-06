package com.co.prueba.contact

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.co.prueba.R
import com.co.prueba.home.HomeActivity
import com.co.prueba.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ContactActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtLastName: EditText
    private lateinit var btnCreate: Button
    private lateinit var btnUpdate: Button
    private lateinit var btnSent: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var progressbar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        setSupportActionBar(findViewById(R.id.toolbar))

        edtName= findViewById(R.id.edtName)
        edtEmail= findViewById(R.id.edtEmail)
        edtPhone= findViewById(R.id.edtPhone)
        edtLastName= findViewById(R.id.edtLastName)

        btnCreate= findViewById(R.id.btnCreate)
        btnUpdate= findViewById(R.id.btnUpdate)
        btnSent= findViewById(R.id.btnSent)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("Contacts")
        progressbar = findViewById(R.id.progressBar)
        btnCreate.visibility = View.VISIBLE
    }

    fun create(view: View){
        createContact()
    }

    private fun createContact(){
        val name: String= edtName.text.toString()
        val email: String= edtEmail.text.toString()
        val phone: String= edtPhone.text.toString()
        val lastName: String= edtLastName.text.toString()

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(lastName)){
            val contactID = dbReference.push().key

            val contact = Contact(contactID.toString(), name, lastName, email, phone)

            if (contactID != null) {
                dbReference.child(contactID).setValue(contact).addOnCompleteListener{
                    Toast.makeText(applicationContext, "Contact saved successfully", Toast.LENGTH_LONG).show()
                }
                action()
            }
        }

    }


    private fun action(){
        startActivity(Intent(this, HomeActivity::class.java))
    }
}