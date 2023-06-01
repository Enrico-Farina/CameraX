package com.example.aplicacaoteste

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import com.example.aplicacaoteste.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btncamera.setOnClickListener{
            cameraProviderResult.launch(android.Manifest.permission.CAMERA)
        }
    }

    private val cameraProviderResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                var intent: Intent = Intent(this, Tela2Activity::class.java)
                startActivity(intent)
            } else {
                Snackbar.make(
                    binding.root,
                    "Você não concedeu permissões para prosseguir",
                    Snackbar.LENGTH_INDEFINITE
                ).show()
            }
        }

}

