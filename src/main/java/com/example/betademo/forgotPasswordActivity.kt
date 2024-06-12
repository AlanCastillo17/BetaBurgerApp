package com.example.betademo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class forgotPasswordActivity : AppCompatActivity() {
    private var editTextEmailForget : EditText?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        editTextEmailForget= findViewById(R.id.edtTextEmailForget)
    }

    fun ToForget(view: View) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(editTextEmailForget!!.text.toString())
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Correo enviado", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "Correo no valido", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}