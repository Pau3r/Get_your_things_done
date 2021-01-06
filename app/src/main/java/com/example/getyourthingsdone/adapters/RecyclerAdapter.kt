package com.example.getyourthingsdone.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getyourthingsdone.R
import com.example.getyourthingsdone.models.NoteList
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.NoteHolder>() {

    class NoteHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener{
        public var title: TextView = itemView.findViewById(R.id.text_title)
        public var description: TextView = itemView.findViewById(R.id.text_describtion)
        public var date: TextView = itemView.findViewById(R.id.text_date)

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = itemView.context
           // val showPhotoIntent = Intent(context, PhotoActivity::class.java)
            //showPhotoIntent.putExtra(PHOTO_KEY, photo)
            //context.startActivity(showPhotoIntent)
        }

        companion object{
            private  val NOTE_KEY = "NOTE"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.NoteHolder {

        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.recyclerview_item_note,
            parent,
            false
        )
        return NoteHolder(inflatedView)
        }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapter.NoteHolder, position: Int) {
        holder.title.text = NoteList.list[position].title
        holder.description.text = NoteList.list[position].description
        if (NoteList.list[position].date != null){
        holder.date.text = NoteList.list[position].date?.get(Calendar.DAY_OF_MONTH).toString() +
                //java uses month form 0 and undecimber  <facepalm>
                "/" + NoteList.list[position].date?.get(Calendar.MONTH)?.plus(1).toString() + //OMG
                "/" + NoteList.list[position].date?.get(Calendar.YEAR).toString() +
                " " + NoteList.list[position].date?.get(Calendar.HOUR_OF_DAY).toString() +
                ":" + NoteList.list[position].date?.get(Calendar.MINUTE).toString() }
    }

    override fun getItemCount(): Int {
        return NoteList.list.size
    }
}