package com.example.apkprofile

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Calculator : AppCompatActivity() {
    private lateinit var display: TextView
    private lateinit var operatorDisplay: TextView

    private var currentInput = ""
    private var lastOperator = ""
    private var result: Double? = null
    private var isOperatorClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        display = findViewById(R.id.display)
        operatorDisplay = findViewById(R.id.operator_display)

        val buttons = listOf(
            R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
            R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
            R.id.btn_add, R.id.btn_subtract, R.id.btn_multiply, R.id.btn_divide,
            R.id.btn_clear, R.id.btn_equals
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener {
                playButtonSound()
                handleButtonClick(it as Button)
            }
        }

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            playButtonSound()
            finish()
        }
    }

    private fun playButtonSound() {
        val mediaPlayer = MediaPlayer.create(this, R.raw.button_click)
        mediaPlayer.setOnCompletionListener { it.release() }
        mediaPlayer.start()
    }

    private fun handleButtonClick(button: Button) {
        val buttonText = button.text.toString()

        when (buttonText) {
            "C" -> clear()
            "=" -> calculateResult()
            "+", "-", "x", "/" -> handleOperator(buttonText)
            else -> handleNumber(buttonText)
        }
    }

    private fun clear() {
        currentInput = ""
        lastOperator = ""
        result = null
        isOperatorClicked = false
        display.text = ""
        operatorDisplay.text = ""
    }

    private fun handleNumber(number: String) {
        if (isOperatorClicked) {
            currentInput = number
            isOperatorClicked = false
        } else {
            currentInput += number
        }
        display.text = currentInput
    }

    private fun handleOperator(operator: String) {
        if (currentInput.isNotEmpty()) {
            if (result == null) {
                result = currentInput.toDouble()
            } else if (lastOperator.isNotEmpty()) {
                calculateIntermediateResult()
            }
        }
        lastOperator = operator
        isOperatorClicked = true
        operatorDisplay.text = operator
    }

    private fun calculateIntermediateResult() {
        currentInput.toDoubleOrNull()?.let {
            when (lastOperator) {
                "+" -> result = (result ?: 0.0) + it
                "-" -> result = (result ?: 0.0) - it
                "x" -> result = (result ?: 0.0) * it
                "/" -> if (it != 0.0) result = (result ?: 0.0) / it
            }
        }
    }

    private fun calculateResult() {
        if (currentInput.isNotEmpty() && lastOperator.isNotEmpty()) {
            calculateIntermediateResult()
            display.text = result?.toString() ?: "0"
            operatorDisplay.text = ""
            currentInput = result?.toString() ?: ""
            lastOperator = ""
            result = null
        }
    }
}
