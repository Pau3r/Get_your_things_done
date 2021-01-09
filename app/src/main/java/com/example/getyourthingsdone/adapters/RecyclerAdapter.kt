package com.example.getyourthingsdone.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.getyourthingsdone.R
import com.example.getyourthingsdone.models.NoteList
import java.util.*

class NoteHolder(v: View) : RecyclerView.ViewHolder(v) {
    public var title: TextView = v.findViewById(R.id.text_title)
    public var description: TextView = v.findViewById(R.id.text_describtion)
    public var date: TextView = v.findViewById(R.id.text_date)


    fun bind(position: Int, clickListener: OnRecyclerItemClickListener) {
        itemView.setOnClickListener {
            clickListener.onRecyclerItemClicked(position)
        }
    }

}

class RecyclerAdapter(private val itemClickListener: OnRecyclerItemClickListener) : RecyclerView.Adapter<NoteHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {

        val inflatedView = LayoutInflater.from(parent.context).inflate(
                R.layout.recyclerview_item_note,
                parent,
                false
        )
        return NoteHolder(inflatedView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.title.text = NoteList.list[position].title
        holder.description.text = NoteList.list[position].description
        if (NoteList.list[position].startDate != null) {

            //need to add 0 if minutes < 10
            if (NoteList.list[position].startDate?.get(Calendar.MINUTE)!! < 10) {
                holder.date.text = NoteList.list[position].startDate?.get(Calendar.DAY_OF_MONTH).toString() +
                        //java uses month form 0 and undecimber  <facepalm>
                        "/" + NoteList.list[position].startDate?.get(Calendar.MONTH)?.plus(1).toString() + //OMG
                        "/" + NoteList.list[position].startDate?.get(Calendar.YEAR).toString() +
                        " " + NoteList.list[position].startDate?.get(Calendar.HOUR_OF_DAY).toString() +
                        ":0" + NoteList.list[position].startDate?.get(Calendar.MINUTE).toString()
            } else {
                holder.date.text = NoteList.list[position].startDate?.get(Calendar.DAY_OF_MONTH).toString() +
                        //java uses month form 0 and undecimber  <facepalm>
                        "/" + NoteList.list[position].startDate?.get(Calendar.MONTH)?.plus(1).toString() + //OMG
                        "/" + NoteList.list[position].startDate?.get(Calendar.YEAR).toString() +
                        " " + NoteList.list[position].startDate?.get(Calendar.HOUR_OF_DAY).toString() +
                        ":" + NoteList.list[position].startDate?.get(Calendar.MINUTE).toString()
            }
        } else {
            holder.date.text = ""
        }

        holder.bind(position, itemClickListener)
    }

    override fun getItemCount(): Int {
        return NoteList.list.size
    }
}

/**
 * Allows to set up onclick listener in different class
 */
interface OnRecyclerItemClickListener {
    fun onRecyclerItemClicked(position: Int)
}