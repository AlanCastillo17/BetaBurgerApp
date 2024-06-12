package com.example.betademo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private var edName : EditText?=null
    private var edNit : EditText?=null
    private var edEmail : EditText?=null
    private var edPassword : EditText?=null
    private var chbPolicies : CheckBox?=null
    private val text_Pattern : Pattern = Pattern.compile(
        "[a-zA-Z]*"
    )
    private val password_Pattern : Pattern = Pattern.compile(
        "^"+
                "(?=.*[0-9])"+
                "(?=.*[A-Z])"+
                "(?=.*[a-z])"+
                "(?=.*[@#$%^&+=])"+
                "(?=\\S+$)"+
                ".{8,}"+
                "$"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        edName= findViewById(R.id.edName)
        edNit= findViewById(R.id.edNit)
        edEmail= findViewById(R.id.edEmail)
        edPassword= findViewById(R.id.edtPasswordReg)
        chbPolicies= findViewById(R.id.chb_Policies)
    }

    fun onRegister(view: View) {
        if (ValidateForm())
        {
            Toast.makeText(this, "Correcto", Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
        }

    }
    private fun ValidateForm(): Boolean
    {
        var validate=true
        val nameInput=edName!!.text.toString()
        val nitInput=edNit!!.text.toString()
        val emailInput=edEmail!!.text.toString()
        val passwordInput=edPassword!!.text.toString()

        if (!chbPolicies!!.isChecked)
        {
            validate=false
        }
        if (TextUtils.isEmpty(edName!!.text.toString()))
        {
            edName!!.error="Requerido"
            validate=false
        }
        else if (!text_Pattern.matcher(nameInput.replace(" ", "")).matches())
        {
            edName!!.error="Nombre no valido"
            validate=false
        }
        else edName!!.error=null

        if (TextUtils.isEmpty(edPassword!!.text.toString()))
        {
            edPassword!!.error="Requerido"
            validate=false
        }
        else if(!password_Pattern.matcher(passwordInput).matches())
        {
            edPassword!!.error="Contraseña no valida"


        }else edPassword!!.error=null

        return validate
    }
}