package com.rdo.wardenhelper

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val calculatorButton: Button = findViewById(R.id.button_calculator)
        calculatorButton.setOnClickListener {
            val intent = Intent(this, Calculator::class.java)
            startActivity(intent)
        }

        val prisonersButton: Button = findViewById(R.id.button_prisoners)
        prisonersButton.setOnClickListener {
            val intent = Intent(this, Prisoners::class.java)
            startActivity(intent)
        }
    }
}