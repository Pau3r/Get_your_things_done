package com.example.getyourthingsdone.activities

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import android.widget.*
import com.example.getyourthingsdone.R
import com.example.getyourthingsdone.models.Note
import com.example.getyourthingsdone.models.NoteList
import com.example.getyourthingsdone.models.SavePreferences
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class EditNoteActivity : AppCompatActivity() {

    private lateinit var mTitle: EditText
    private lateinit var mDescription: EditText
    private lateinit var mEventDurationTyped: EditText
    private lateinit var mDate: TextView
    private val mChosenDate = Calendar.getInstance()

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private lateinit var mSwitchDate: Switch
    private var mPosition: Int = -1
    private var mIsNewNote: Boolean = true
    private var mEventDuration: Long = 900000
    //TODO Let user change event duration


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        //setSupportActionBar(findViewById(R.id.toolbar))

        if (intent != null && intent.extras != null) {

            mPosition = intent.extras!!["position"] as Int
            mIsNewNote = false

        }

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener { view ->
            if (mTitle.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.no_title, Toast.LENGTH_SHORT).show()
            } else {
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
            if (!mIsNewNote) {
                deleteNote(mPosition)
            }

            startActivity(
                Intent(
                    this,
                    NotesActivity::class.java
                )
            )
        }

        findViewById<FloatingActionButton>(R.id.fabAddCalendarEvent).setOnClickListener { view ->
            if (mIsNewNote) {
                if (mTitle.text.toString().isEmpty()) {
                    Toast.makeText(this, R.string.no_title, Toast.LENGTH_SHORT).show()
                } else {
                    addNote()
                    mIsNewNote = false
                    mPosition = NoteList.list.size - 1
                    addEventToCalendar()
                }
            } else {
                NoteList.list[mPosition].endDate = setEndDate()
                addEventToCalendar()
            }
        }

        mTitle = findViewById(R.id.editTextTitle)
        mDescription = findViewById(R.id.editTextDescription)
        mDate = findViewById(R.id.editTextDate)
        mSwitchDate = findViewById(R.id.switchDate)
        mEventDurationTyped = findViewById(R.id.editTextEventLength)

        if (!mIsNewNote) {

            mTitle.text.append(NoteList.list[mPosition].title)
            if (NoteList.list[mPosition].description != null) mDescription.text.append(NoteList.list[mPosition].description)
            if (NoteList.list[mPosition].startDate != null) {
                mDate.text =
                    NoteList.list[mPosition].startDate?.get(Calendar.DAY_OF_MONTH).toString() +
                            //java uses month form 0 and undecimber  <facepalm>
                            "/" + NoteList.list[mPosition].startDate?.get(Calendar.MONTH)?.plus(1)
                        .toString() + //OMG
                            "/" + NoteList.list[mPosition].startDate?.get(Calendar.YEAR)
                        .toString() +
                            " " + NoteList.list[mPosition].startDate?.get(Calendar.HOUR_OF_DAY)
                        .toString() +
                            ":" + NoteList.list[mPosition].startDate?.get(Calendar.MINUTE)
                        .toString()
                //set date from list
                mChosenDate.set(
                    NoteList.list[mPosition].startDate?.get(Calendar.YEAR)!!,
                    NoteList.list[mPosition].startDate?.get(Calendar.MONTH)!!,
                    NoteList.list[mPosition].startDate?.get(Calendar.DAY_OF_MONTH)!!,
                    NoteList.list[mPosition].startDate?.get(Calendar.HOUR_OF_DAY)!!,
                    NoteList.list[mPosition].startDate?.get(Calendar.MINUTE)!!
                )

                mSwitchDate.isChecked = false
                mEventDurationTyped.visibility = View.VISIBLE
                val time =
                    (NoteList.list[mPosition].endDate?.timeInMillis?.minus(NoteList.list[mPosition].startDate?.timeInMillis!!))?.div(
                        60000
                    )
                mEventDurationTyped.text.append(time.toString())

                addDateToNote()

            }

        }

        mSwitchDate.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                addDateToNote()
                mEventDurationTyped.visibility = View.VISIBLE
            } else {
                mDate.setOnClickListener(null)
                mEventDurationTyped.visibility = View.INVISIBLE
            }
        }


    }


    @SuppressLint("SetTextI18n")
    private fun addDateToNote() {
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
                { view, year, month, dayOfMonth ->
                    //need to add plus to to month because months start from 0
                    mDate.text = "$dayOfMonth/${month.plus(1)}/$year"
                    mChosenDate.set(year, month, dayOfMonth)
                    timePickerDialog(hour, minute)
                },
                year,
                month,
                day
            ).show()
        }
    }

    private fun addNote() {
        if (mIsNewNote) {
            if (!mSwitchDate.isChecked) {
                NoteList.list += Note(
                    mTitle.text.toString(),
                    mDescription.text.toString(),
                    mChosenDate,
                    setEndDate()
                )
            } else {
                NoteList.list += Note(
                    mTitle.text.toString(),
                    mDescription.text.toString(),
                    null,
                    null
                )
            }
        } else {
            if (!mSwitchDate.isChecked) {
                NoteList.list[mPosition] = Note(
                    mTitle.text.toString(),
                    mDescription.text.toString(),
                    mChosenDate,
                    setEndDate()
                )
            } else {
                NoteList.list[mPosition] =
                    Note(mTitle.text.toString(), mDescription.text.toString(), null, null)
            }

        }

        saveListToSharedPreferences()


    }

    private fun deleteNote(position: Int) {

        NoteList.list.removeAt(position)
        saveListToSharedPreferences()
    }

    private fun timePickerDialog(hour: Int, minute: Int) {
        TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            //less than 10 minutes appears as X instead of 0X
            if (minute < 10) {
                mDate.append(" $hourOfDay:0$minute")
            } else {
                mDate.append(" $hourOfDay:$minute")
            }

            mChosenDate.set(Calendar.MINUTE, minute)
            mChosenDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
        }, hour, minute, true).show()
    }

    private fun saveListToSharedPreferences() {
        val sharedPreferences = getSharedPreferences(
            resources.getString(R.string.shared_preferences_list),
            MODE_PRIVATE
        )
        val savePreferences = SavePreferences(sharedPreferences)
        savePreferences.saveNoteList()
    }


    private fun setEndDate(): Calendar {
        if (mEventDurationTyped.text.isNotEmpty()) {
            mEventDuration = (mEventDurationTyped.text.toString().toLong() * 60000)
        }

        val endDateInMilis = mChosenDate.timeInMillis + mEventDuration

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = endDateInMilis
        return calendar
    }

    private fun addEventToCalendar() {

        val note = NoteList.list[mPosition]

        val calendarIntent = Intent(Intent.ACTION_INSERT)
            .setData(CalendarContract.Events.CONTENT_URI)
            .putExtra(CalendarContract.Events.TITLE, note.title)
            .putExtra(CalendarContract.Events.DESCRIPTION, note.description)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, note.startDate?.timeInMillis)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, note.endDate?.timeInMillis)
        startActivity(calendarIntent)

    }


}