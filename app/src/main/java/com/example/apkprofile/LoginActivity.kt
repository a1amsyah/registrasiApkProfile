package com.example.apkprofile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.apkprofile.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inisialisasi binding dengan benar
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Pastikan binding.root digunakan jika ID "main" tidak ada di XML
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        databaseHelper = DatabaseHelper(this)

        binding.buttonLogin.setOnClickListener {
            val loginUsername = binding.editTextTextEmailAddress.text.toString().trim()
            val loginPassword = binding.editTextTextPassword2.text.toString().trim()

            if (loginUsername.isEmpty() || loginPassword.isEmpty()) {
                Toast.makeText(this, "Harap isi username dan password", Toast.LENGTH_SHORT).show()
            } else {
                loginDatabase(loginUsername, loginPassword)
            }
        }

        binding.Register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loginDatabase(username: String, password: String) {
        val userExists = databaseHelper.readUser(username, password)
        if (userExists) {
            Toast.makeText(this, "Login Sukses", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Username dan password salah", Toast.LENGTH_SHORT).show()
        }
    }
}
