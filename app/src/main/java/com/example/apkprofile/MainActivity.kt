package com.example.apkprofile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.apkprofile.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setup()
    }

    private fun setup() {
        binding.textView3.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("APK Profile")
            builder.setMessage("Tentang Saya : Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent non est sit amet sem scelerisque venenatis. Fusce est nisi, lacinia. ")
            builder.setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
            Toast.makeText(applicationContext, "Tentang Saya", Toast.LENGTH_LONG).show()
        }

        // Menambahkan klik event pada CardView
        binding.cardCalculator.setOnClickListener {
            val intent = Intent(this, Calculator::class.java)
            startActivity(intent)
        }

        binding.btnKeluar.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
