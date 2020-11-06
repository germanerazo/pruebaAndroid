package com.co.prueba.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        val actionBar = supportActionBar
        actionBar!!.title = "Register"

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

        edtName.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })

        edtEmail.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })

        edtPhone.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })

        edtPassword.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })

        edtConfirmPassword.setOnFocusChangeListener(OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                hideKeyboard(v)
            }
        })

    }

    fun register(view: View){
        createAcount()
    }

    private fun createAcount(){
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
                    if(task.isSuccessful){
                        Log.d("isSuccessful", "createUserWithEmail:success")
                        val user:FirebaseUser?=auth.currentUser
                        Log.e("user", user?.uid.toString())
                        sentEmail(user)

                        val userDB= user?.uid?.let { dbReference.child(it) }
                        userDB?.child("id")?.setValue(user?.uid)
                        userDB?.child("name")?.setValue(name)
                        userDB?.child("phone")?.setValue(phone)
                        userDB?.child("email")?.setValue(email)
                        action()
                    }else{
                        Log.w("ERROR", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }else{
            Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_LONG).show()
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

    fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


}