package com.example.getyourthingsdone.models

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SavePreferences(private val sharedPreferences:  SharedPreferences) {

    fun saveNoteList(){
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(NoteList.list)
        editor.putString("noteList",json)
        editor.apply()
        Log.i("json",json)
    }
    fun readNoteList(){
        if (sharedPreferences.contains("noteList")){
            val gson = Gson()
            val json = sharedPreferences.getString("noteList","")
            Log.i("json",json)
            val listType = genericType<ArrayList<Note>>()
            NoteList.list.clear()
            NoteList.list.addAll(gson.fromJson(json,listType))


        }
    }

    inline fun <reified T> genericType() = object: TypeToken<T>() {}.type

}