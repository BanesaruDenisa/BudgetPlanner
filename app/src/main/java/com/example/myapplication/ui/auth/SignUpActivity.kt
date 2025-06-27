package com.example.myapplication.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.data.model.User
import com.example.myapplication.data.dao.UserDao
import com.example.myapplication.data.database.AppDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var signupEmail: EditText
    private lateinit var signupPassword: EditText
    private lateinit var signupButton: Button
    private lateinit var loginRedirectText: TextView

    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        signupEmail = findViewById(R.id.signup_email)
        signupPassword = findViewById(R.id.signup_password)
        signupButton = findViewById(R.id.signup_button)
        loginRedirectText = findViewById(R.id.loginRedirectText)

        userDao = AppDatabase.getDatabase(this).userDao()

        signupButton.setOnClickListener {
            val email = signupEmail.text.toString().trim()
            val password = signupPassword.text.toString().trim()

            if (email.isEmpty()) {
                signupEmail.error = "Email cannot be empty"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                signupEmail.error = "Please enter a valid email"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                signupPassword.error = "Password cannot be empty"
                return@setOnClickListener
            }

            //  Verificăm dacă userul există deja
            val existing = userDao.authenticate(email, password)
            if (existing != null) {
                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUser = User(email, password)
            userDao.insert(newUser)

            Toast.makeText(this, "SignUp Successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loginRedirectText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
