package com.example.getyourthingsdone.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.getyourthingsdone.R
import com.example.getyourthingsdone.adapters.OnRecyclerItemClickListener
import com.example.getyourthingsdone.adapters.RecyclerAdapter
import com.example.getyourthingsdone.models.SavePreferences
import com.example.getyourthingsdone.services.AppKillService
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesActivity : AppCompatActivity(), OnRecyclerItemClickListener {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        setSupportActionBar(findViewById(R.id.toolbar))
        startAppkillservice()
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            startActivity(
                    Intent(
                            this,
                            EditNoteActivity::class.java
                    )
            )
        }

        mRecyclerView = findViewById(R.id.recycler_notes)


        linearLayoutManager = LinearLayoutManager(this)
        mRecyclerView.layoutManager = linearLayoutManager
        mAdapter = RecyclerAdapter(this)
        mRecyclerView.adapter = mAdapter

    }

    override fun onResume() {
        super.onResume()
        mAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRecyclerItemClicked(position: Int) {
        val intent = Intent(this, EditNoteActivity::class.java)
        intent.putExtra("position", position)
        startActivity(intent)

    }

    private fun startAppkillservice() {
        val sharedPreferences = getSharedPreferences(resources.getString(R.string.shared_preferences_list), MODE_PRIVATE)
        val savePreferences = SavePreferences(sharedPreferences)
        savePreferences.readNoteList()
        val appKillService = Intent(this, AppKillService::class.java)
        startService(appKillService)
    }


}