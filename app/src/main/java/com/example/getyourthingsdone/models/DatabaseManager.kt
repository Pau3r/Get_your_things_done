package com.example.getyourthingsdone.models

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import java.util.*
import kotlin.collections.HashMap


class DatabaseManager {
    private val db = Firebase.firestore
    private val user = FirebaseAuth.getInstance().currentUser!!.uid


    fun addNewNoteToDatabase(position: Int) {
        val note = NoteList.list[position]
        db.collection(user).add(note).addOnSuccessListener { documentReference ->
            Log.w("Firebase", "DocumentSnapshot added with ID:${documentReference.id}")
        }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error adding document", e)
            }

    }

    fun addAllNotesToDatabase() {
        //deleteAllNotesFromDatabase()
        for (note in NoteList.list) {
            db.collection(user).add(note).addOnSuccessListener { documentReference ->
                Log.w("Firebase", "DocumentSnapshot added with ID:${documentReference.id}")
            }
                .addOnFailureListener { e ->
                    Log.w("Firebase", "Error adding document", e)
                }

        }
    }


    fun getAllNotesFromDatabase() {
        NoteList.list.clear()
        db.collection(user).get().addOnSuccessListener {
            for (savedNote in it) {

                if (savedNote["startDate"] != null && savedNote["endDate"] != null) {
                    val startCalendar = Calendar.getInstance()
                    val endCalendar = Calendar.getInstance()
                    val startDateSnapshot = savedNote["startDate"] as HashMap<*, *>
                    val endDateSnapshot = savedNote["endDate"] as HashMap<*, *>


                    startCalendar.timeInMillis = startDateSnapshot["timeInMillis"] as Long
                    endCalendar.timeInMillis = endDateSnapshot["timeInMillis"] as Long

                    NoteList.list += Note(
                        savedNote["title"] as String,
                        savedNote["description"] as String, startCalendar, endCalendar
                    )
                } else
                    NoteList.list += Note(
                        savedNote["title"] as String,
                        savedNote["description"] as String, null, null
                    )
            }

        }.addOnFailureListener { e ->
            Log.w("Firebase", "Error reading document", e)
        }

    }

    fun deleteAllNotesFromDatabase() {
        db.collection(user)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    document.reference.delete()
                }
            }

    }

    fun deleteNotesWithNameFromDatabase(position: Int) {
        db.collection(user).whereEqualTo("name", NoteList.list[position].title).get()
            .addOnSuccessListener { document ->
                for (doc in document.documents) {
                    doc.reference.delete()
                }
            }


    }

    fun checkUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }


}