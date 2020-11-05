package com.co.prueba.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.co.prueba.R
import com.co.prueba.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnRegister: Button
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var progressbar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        edtName= findViewById(R.id.edtName)
        edtEmail= findViewById(R.id.edtEmail)
        edtPhone= findViewById(R.id.edtPhone)
        edtPassword= findViewById(R.id.edtPassword)
        edtConfirmPassword= findViewById(R.id.edtConfirmPassword)
        btnRegister= findViewById(R.id.btnRegister)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("User")
        progressbar = findViewById(R.id.progressBar)

    }

    fun register(view: View){
        createAcoount()
    }

    private fun createAcoount(){
        val name: String= edtName.text.toString()
        val email: String= edtEmail.text.toString()
        val phone: String= edtPhone.text.toString()
        val password: String= edtPassword.text.toString()
        val confirmPassord: String= edtConfirmPassword.text.toString()

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassord)  ){
            if (password == confirmPassord){
                 progressbar.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this){
                    task ->
                    if(task.isComplete){
                        val user:FirebaseUser?=auth.currentUser
                        sentEmail(user)

                        val userDB= user?.uid?.let { dbReference.child(it) }
                        userDB?.child("Name")?.setValue(name)
                        userDB?.child("Phone")?.setValue(phone)
                        action()
                    }
                }

            }
        }
    }

    private fun sentEmail(user:FirebaseUser?){
        user?.sendEmailVerification()?.addOnCompleteListener(this){ task ->
            if (task.isComplete){
                Toast.makeText(this, "Email sent", Toast.LENGTH_LONG)
            }else{
                Toast.makeText(this, "Mail not sent", Toast.LENGTH_LONG)
            }
        }
    }

    private fun action(){
        startActivity(Intent(this, LoginActivity::class.java))
    }


}