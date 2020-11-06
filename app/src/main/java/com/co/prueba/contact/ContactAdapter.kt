package com.co.prueba.contact

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.co.prueba.R
import com.co.prueba.messages.Messages
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

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
            sentMessage(contact)
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

                val userId = FirebaseAuth.getInstance().uid

                val contact = Contact(contact.id,name1,lastname2,email3,phone4, userId.toString(),contact.idContact)
                myDatabase.child(contact.idContact).setValue(contact)
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
        myDatabase.child(contact.idContact).removeValue()
        Toast.makeText(mCtx,"Deleted",Toast.LENGTH_LONG).show()
    }

    private fun sentMessage(contact: Contact){

        val builder = AlertDialog.Builder(mCtx)
        builder.setTitle(contact.name)
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.sentmessage,null)
        val adapter = GroupAdapter<ViewHolder>()

        readMessagesDB(adapter, contact)

        val btnSend = view.findViewById<Button>(R.id.btnSend)
        val reciclerChat = view.findViewById<RecyclerView>(R.id.reciclerChat)
        val edtMessage = view.findViewById<EditText>(R.id.edtMessage)
        reciclerChat.adapter = adapter

        btnSend.setOnClickListener{
            sendMessageDB(edtMessage.text.toString(), contact.id )
            edtMessage.text = null
        }


        builder.setView(view)

        val alert = builder.create()
        alert.show()

    }

    fun sendMessageDB(message: String, toId: String){

        val database = FirebaseDatabase.getInstance()
        val dbReference = database.reference.child("Messages")
        val messageID = dbReference.push().key
        val fromId = FirebaseAuth.getInstance().uid

        val text = Messages(messageID.toString(), fromId.toString(),toId, message, System.currentTimeMillis()/1000)

        if (messageID != null) {
            dbReference.child(messageID).setValue(text).addOnCompleteListener{
            }
        }
    }

    private fun readMessagesDB(adapter: GroupAdapter<ViewHolder>, contact: Contact){
        val database = FirebaseDatabase.getInstance()
        val dbReference = database.reference.child("Messages")

        dbReference.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chat = snapshot.getValue(Messages::class.java)

                if (chat != null) {
                    if (chat.fromId.toString() == FirebaseAuth.getInstance().uid && chat.toId.toString() == contact.id){
                        adapter.add(ChatToItem(chat?.message.toString(), contact.name))
                    }else if (chat.fromId.toString() == contact.id && chat.toId.toString() == FirebaseAuth.getInstance().uid){
                        adapter.add(ChatFromItem(chat?.message.toString()))
                    }
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

        })
    }

}

class ChatFromItem(val text: String): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int){
        viewHolder.itemView.textFromMessage.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

}

class ChatToItem(val text: String, contact: String): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int){
        viewHolder.itemView.textToMessage.text = text
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

}