package com.co.prueba.forgot

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.co.prueba.R
import com.co.prueba.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotActivity : AppCompatActivity() {


    private lateinit var edtEmail: EditText
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        setSupportActionBar(findViewById(R.id.toolbar))

        edtEmail= findViewById(R.id.edtEmail)
        auth = FirebaseAuth.getInstance()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    fun send(view: View){
        val email: String= edtEmail.text.toString()
        if(!TextUtils.isEmpty(email)){
            auth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){
                    action()
                }else{
                    Log.w("ERROR", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun action(){
        startActivity(Intent(this, LoginActivity::class.java))
    }
}