package com.example.myapplication.ui.auth

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.data.dao.UserDao
import com.example.myapplication.data.database.AppDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var signupRedirectText: TextView
    private lateinit var loginButton: Button
    private lateinit var forgotPassword: TextView

    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginEmail = findViewById(R.id.login_email)
        loginPassword = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        signupRedirectText = findViewById(R.id.signUpRedirectText)
        forgotPassword = findViewById(R.id.forgot_password)

        // Inițializare Room DAO
        userDao = AppDatabase.getDatabase(this).userDao()

        loginButton.setOnClickListener {
            val email = loginEmail.text.toString().trim()
            val pass = loginPassword.text.toString().trim()

            if (email.isEmpty()) {
                loginEmail.error = "Email cannot be empty"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginEmail.error = "Please enter a valid email"
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                loginPassword.error = "Password cannot be empty"
                return@setOnClickListener
            }

            val user = userDao.authenticate(email, pass)
            if (user != null) {
                // ✅ Salvăm și emailul logat în SharedPreferences
                val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                sharedPref.edit()
                    .putBoolean("isLoggedIn", true)
                    .putString("loggedInUserEmail", user.email)
                    .apply()

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        signupRedirectText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        forgotPassword.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.dialog_forgot, null)
            val emailBox = dialogView.findViewById<EditText>(R.id.emailBox)

            builder.setView(dialogView)
            val dialog = builder.create()

            dialogView.findViewById<View>(R.id.btnReset).setOnClickListener {
                val userEmail = emailBox.text.toString().trim()

                if (userEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    Toast.makeText(this, "Enter your registered email id", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val exists = userDao.authenticate(userEmail, "dummy") != null
                if (exists) {
                    Toast.makeText(this, "Simulated password reset. Implement logic if needed.", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            dialogView.findViewById<View>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            dialog.window?.setBackgroundDrawable(ColorDrawable(0))
            dialog.show()
        }
    }
}
