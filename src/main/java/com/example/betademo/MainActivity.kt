package com.example.betademo

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : AppCompatActivity() {
    private val GOOGLE_SIGN_IN=100
    private var edtUsername: EditText?=null
    private var edtPassword: EditText?=null
    private var authLayout: LinearLayout?=null
    private var btnGoogle : SignInButton?=null
    private var txtViewForgotPassword : TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        authLayout= findViewById(R.id.authLayout)
        btnGoogle= findViewById(R.id.btnGoogle)
        txtViewForgotPassword= findViewById(R.id.txtViewForgotPassword)

        session()
        loginGoogle()

        txtViewForgotPassword!!.setOnClickListener {
            val intent = Intent(this,forgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun loginGoogle() {
        btnGoogle!!.setOnClickListener {
            val googleleconf : GoogleSignInOptions=GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient : GoogleSignInClient = GoogleSignIn.getClient(
                this, googleleconf
            )
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)
        }
    }

    fun onLogin(btnLogin: View){

        val messagepassword=getString(R.string.messagepassword)
        val messageusername=getString(R.string.messageusername)
        var username : String=edtUsername!!.text.toString()

        if (username=="alan@correo.co")
        {
            if(edtPassword!!.text.toString()=="12345")
            {
                val negativeButton={_:DialogInterface,_:Int->}
                val positiveButton={dialog:DialogInterface,which:Int->
                    val intent = Intent(this, WelcomeActivity::class.java)
                    startActivity(intent)
                }

                val dialog=AlertDialog.Builder(this)
                                      .setTitle("Welcome")
                                      .setMessage("User: " + username)
                                      .setPositiveButton("ok",positiveButton)
                                      .setNegativeButton("Cancel",negativeButton)
                                      .create()
                                      .show()
            }
            else {
                val dialog = AlertDialog.Builder(this)
                                        .setTitle("ERROR")
                                        .setMessage(messagepassword)
                                        .create()
                                        .show()
            }
        }
        else {
            Toast.makeText(this, messageusername, Toast.LENGTH_LONG)
                 .show()
        }
    }

    fun onRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun onRegisterEmail(view: View) {
        title="Autentification"
        if(edtUsername!!.text.isNotEmpty() && edtPassword!!.text.isNotEmpty()){
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                edtUsername!!.text.toString(),
                edtPassword!!.text.toString()
            ).addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "correcto", Toast.LENGTH_LONG).show()
                    showHome(it.result?.user?.email?:"",ProviderType.BASIC)
                }else{
                    showAlert()
                }
            }
        }
    }
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autentificando el usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email : String, provider : ProviderType){
        val homeIntent =Intent(this, WelcomeActivity::class.java)
            .apply {
                putExtra("email", email)
                putExtra("provider", provider.name)
            }
        startActivity(homeIntent)
    }

    fun onLoginEmail(view: View) {
        title="Autentification"
        if(edtUsername!!.text.isNotEmpty() && edtPassword!!.text.isNotEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                edtUsername!!.text.toString(),
                edtPassword!!.text.toString()
            ).addOnCompleteListener{
                if(it.isSuccessful){
                    //Toast.makeText(this, "correct", Toast.LENGTH_LONG).show()
                    showHome(it.result?.user?.email?:"",ProviderType.BASIC)
                }else{
                    showAlert()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        authLayout!!.visibility=View.VISIBLE
    }
    private fun session(){
        val prefs : SharedPreferences=getSharedPreferences(
            getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )
        val email : String? = prefs.getString("email",null)
        val provider: String? = prefs.getString("provider",null)
        if(email != null && provider!=null){
            authLayout!!.visibility=View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
        FirebaseAuth.getInstance().signOut()
        onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==GOOGLE_SIGN_IN){
            val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account : GoogleSignInAccount? = task.getResult(ApiException::class.java)
                if(account != null){
                    val credential : AuthCredential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            showHome(account.email ?:"",ProviderType.GOOGLE)
                        }else{
                            showAlert()
                        }
                    }
                }
            }catch (e: ApiException){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage(e.toString())
                builder.setPositiveButton("Aceptar",null)
                val dialog : AlertDialog = builder.create()
                dialog.show()
            }
        }
    }
}