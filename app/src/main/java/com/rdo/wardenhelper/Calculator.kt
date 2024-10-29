package com.rdo.wardenhelper

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class Calculator : AppCompatActivity() {

    private var sentenceMinutes = 0
    private lateinit var sentenceTextView: TextView
    private lateinit var nameEditText: EditText
    private var prisonerCount = 1
    private val articles = mutableListOf<String>()
    private lateinit var prisonerStorage: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        sentenceTextView = findViewById(R.id.text_sentence)
        nameEditText = findViewById(R.id.edit_prisoner_name)
        prisonerStorage = getSharedPreferences("prisoners", Context.MODE_PRIVATE)

        // Обработчики кнопок статей КЗ
        findViewById<Button>(R.id.button_xx1).setOnClickListener { addArticle("xx1", 5) }
        findViewById<Button>(R.id.button_xx2).setOnClickListener { addArticle("xx2", 10) }
        findViewById<Button>(R.id.button_xx3).setOnClickListener { addArticle("xx3", 15) }
        findViewById<Button>(R.id.button_xx4).setOnClickListener { addArticle("xx4", 25) }
        findViewById<Button>(R.id.button_xx5).setOnClickListener { addArticle("xx5", -1) } // Пожизненное
        findViewById<Button>(R.id.button_xx6).setOnClickListener { addArticle("xx6", -2) } // Казнь

        // Обработчики кнопок модификаторов
        findViewById<Button>(R.id.button_coming_clean).setOnClickListener { addModifier("Явка с повинной", -5) }
        findViewById<Button>(R.id.button_negligence).setOnClickListener { addModifier("Преступление по неосторожности", -5) }
        findViewById<Button>(R.id.button_voluntary_refusal).setOnClickListener { addModifier("Добровольный отказ", -5) }
        findViewById<Button>(R.id.button_organizer).setOnClickListener { addModifier("Организатор", 5) }
        findViewById<Button>(R.id.button_recidivist).setOnClickListener { addModifier("Рецидив", 5) }
        findViewById<Button>(R.id.button_official_crime).setOnClickListener { addModifier("Преступление против должностного лица", 10) }

        // Сохранение заключенного
        findViewById<Button>(R.id.button_save).setOnClickListener {
            savePrisoner()
            clearFields()
        }

        // Очистка полей
        findViewById<Button>(R.id.button_clear).setOnClickListener {
            clearFields()
        }
    }

    private fun addArticle(article: String, minutes: Int) {
        if (minutes == -1) {
            sentenceTextView.text = "Пожизненное"
            sentenceMinutes = -1
        } else if (minutes == -2) {
            sentenceTextView.text = "Казнь"
            sentenceMinutes = -2
        } else if (sentenceMinutes >= 0) {
            sentenceMinutes += minutes
            sentenceTextView.text = "$sentenceMinutes минут"
            articles.add(article)
        }
    }

    private fun addModifier(modifier: String, minutes: Int) {
        if (sentenceMinutes > 0) {
            sentenceMinutes += minutes
            sentenceTextView.text = "$sentenceMinutes минут"
            articles.add(modifier)
        }
    }

    private fun savePrisoner() {
        val name = if (nameEditText.text.isNotBlank()) nameEditText.text.toString() else "Заключенный $prisonerCount"
        if (nameEditText.text.isBlank()) prisonerCount++

        val sentence = when (sentenceMinutes) {
            -1 -> "Пожизненное"
            -2 -> "Казнь"
            else -> "$sentenceMinutes минут"
        }

        val prisonerData = "$name|${articles.joinToString(", ")}|$sentence"
        val prisoners = prisonerStorage.getStringSet("prisoners_list", mutableSetOf()) ?: mutableSetOf()
        prisoners.add(prisonerData)
        prisonerStorage.edit().putStringSet("prisoners_list", prisoners).apply()
    }

    private fun clearFields() {
        nameEditText.text.clear()
        sentenceMinutes = 0
        sentenceTextView.text = "0 минут"
        articles.clear()
    }
}