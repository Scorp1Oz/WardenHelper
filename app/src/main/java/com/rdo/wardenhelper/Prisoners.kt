package com.rdo.wardenhelper

import android.os.Bundle
import android.content.Context
import android.content.SharedPreferences
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class Prisoners : AppCompatActivity() {

    private lateinit var tablePrisoners: TableLayout
    private lateinit var prisonerStorage: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prisoners)
        tablePrisoners = findViewById(R.id.table_prisoners)
        prisonerStorage = getSharedPreferences("prisoners", Context.MODE_PRIVATE)
        loadPrisoners()
    }

    private fun loadPrisoners() {
        val prisoners = prisonerStorage.getStringSet("prisoners_list", setOf())?.toMutableSet() ?: return
        prisoners.forEach { prisonerData ->
            val prisonerDetails = prisonerData.split("|")
            val row = TableRow(this)
            prisonerDetails.forEach { detail ->
                val cell = TextView(this)
                cell.text = detail
                cell.layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                row.addView(cell)
            }
            val deleteButton = TextView(this).apply {
                text = "✖"
                setOnClickListener { removePrisoner(prisonerData, prisoners) }
            }
            row.addView(deleteButton)
            tablePrisoners.addView(row)
        }
    }

    private fun removePrisoner(prisonerData: String, prisoners: MutableSet<String>) {
        prisoners.remove(prisonerData)
        prisonerStorage.edit().putStringSet("prisoners_list", prisoners).apply()
        tablePrisoners.removeAllViews()
        loadPrisoners()
    }
}