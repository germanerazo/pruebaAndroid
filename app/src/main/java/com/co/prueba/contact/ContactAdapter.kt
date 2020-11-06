package com.co.prueba.contact

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.co.prueba.R
import com.google.firebase.database.FirebaseDatabase

class ContactAdapter(private val mCtx : Context, private val layoutId:Int, private val contactList:List<Contact>)
    : ArrayAdapter<Contact>(mCtx,layoutId,contactList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view:View = layoutInflater.inflate(layoutId,null)

        val name = view.findViewById<TextView>(R.id.textName)
        val lastName = view.findViewById<TextView>(R.id.textLastName)

        val updateBtn = view.findViewById<Button>(R.id.btnUpdate)
        val deleteBtn = view.findViewById<Button>(R.id.btnDelete)
        val sentBtn = view.findViewById<Button>(R.id.btnSent)

        val contact = contactList[position]

        name.text = contact.name
        lastName.text = contact.lastname

        updateBtn.setOnClickListener {
            updateInfo(contact)
        }

        deleteBtn.setOnClickListener {
            deleteInfo(contact)
        }

        sentBtn.setOnClickListener {
            sentMessage()
        }

        return view
    }

    private  fun updateInfo(contact:Contact){
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Update Contact")
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.content_contact,null)

        val name = view.findViewById<TextView>(R.id.edtName)
        val lastname = view.findViewById<TextView>(R.id.edtLastName)
        val email = view.findViewById<TextView>(R.id.edtEmail)
        val phone = view.findViewById<TextView>(R.id.edtPhone)

        name.text = contact.name
        lastname.text = contact.lastname
        email.text = contact.email
        phone.text = contact.phone

        builder.setView(view)

        builder.setPositiveButton("Update",object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

                val  myDatabase = FirebaseDatabase.getInstance().getReference("Contacts")

                val name1    = name.text.toString().trim()
                val lastname2     = lastname.text.toString().trim()
                val email3     = email.text.toString().trim()
                val phone4   = phone.text.toString().trim()

                if (name1.isEmpty()){
                    name.error = "Please enter your name"
                    return
                }
                if (lastname2.isEmpty()){
                    lastname.error = "Please enter your lastname"
                    return
                }
                if (email3.isEmpty()){
                    email.error = "Please enter your email"
                    return
                }
                if (phone4.isEmpty()){
                    phone.error = "Please enter your phone"
                    return
                }

                val contact = Contact(contact.id,name1,lastname2,email3,phone4)
                myDatabase.child(contact.id).setValue(contact)
                Toast.makeText(mCtx,"Updated ", Toast.LENGTH_LONG).show()



            }})

        builder.setNegativeButton("cancel",object :DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }

        })

        val alert = builder.create()
        alert.show()
    }

    private fun deleteInfo(contact:Contact){
        val myDatabase = FirebaseDatabase.getInstance().getReference("Contacts")
        myDatabase.child(contact.id).removeValue()
        Toast.makeText(mCtx,"Deleted",Toast.LENGTH_LONG).show()
    }

    private fun sentMessage(){
        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle("Sent Message")
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.sentmessage,null)
    }

}