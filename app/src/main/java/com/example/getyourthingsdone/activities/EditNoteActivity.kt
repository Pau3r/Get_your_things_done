package com.example.getyourthingsdone.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.*
import com.example.getyourthingsdone.R
import com.example.getyourthingsdone.models.Note
import com.example.getyourthingsdone.models.NoteList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class EditNoteActivity : AppCompatActivity() {

    private lateinit var mTitle: EditText
    private lateinit var mDescription: EditText
    private lateinit var mDate: TextView
    private val mChosenDate = Calendar.getInstance()
    private  lateinit var mSwitchDate: Switch

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        //setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener { view ->
            if (mTitle.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.no_title, Toast.LENGTH_SHORT).show()
            }else {
                addNote()

                startActivity(
                    Intent(
                        this,
                        NotesActivity::class.java
                    )
                )
            }

        }
        findViewById<FloatingActionButton>(R.id.fabDelete).setOnClickListener { view ->
            deleteNote()

                startActivity(
                    Intent(
                        this,
                        NotesActivity::class.java
                    )
                )
        }

        mTitle = findViewById(R.id.editTextTitle)
        mDescription = findViewById(R.id.editTextDescription)
        mDate = findViewById(R.id.editTextDate)
        mSwitchDate = findViewById(R.id.switchDate)

        if (!mSwitchDate.isChecked) {
            //show date picker
            mDate.setOnClickListener {
                val calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                //date picker window
                DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        //need to add plus to to month because months start from 0
                        mDate.setText("$dayOfMonth/${month.plus(1)}/$year")
                        mChosenDate.set(year, month, dayOfMonth)
                        timePickerDialog(hour, minute)
                    },
                    year,
                    month,
                    day
                ).show()

            }
        }


    }



    private fun addNote(){
        if (!mSwitchDate.isChecked) {
        NoteList.list += Note(mTitle.text.toString(), mDescription.text.toString(), mChosenDate)
        }else{
            NoteList.list += Note(mTitle.text.toString(), mDescription.text.toString(), null)
        }

    }

    private  fun deleteNote() {

    }

    private fun timePickerDialog(hour: Int, minute: Int){
        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                view, hourOfDay, minute ->
            mDate.append(  " $hourOfDay:$minute")
            mChosenDate.set(Calendar.MINUTE, minute)
            mChosenDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
        }, hour, minute, true).show()
    }

}