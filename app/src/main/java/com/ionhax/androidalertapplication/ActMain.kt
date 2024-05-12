package com.ionhax.androidalertapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ActMain : AppCompatActivity() {

    private lateinit var mCreateRem: FloatingActionButton
    private lateinit var mRecyclerview: RecyclerView
    private var dataHolder: ArrayList<Model> = ArrayList()
    private lateinit var adapter: myAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_act_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mRecyclerview = findViewById(R.id.recyclerView)
        mRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
        mCreateRem = findViewById(R.id.create_reminder)

        mCreateRem.setOnClickListener {
            val intent = Intent(applicationContext, RemainderActivity::class.java)
            startActivity(intent)
        }

        val cursor = DbManager(applicationContext).readAllReminders()
        while (cursor.moveToNext()) {
            val model = Model(cursor.getString(1), cursor.getString(2), cursor.getString(3))
            dataHolder.add(model)
        }

        adapter = myAdapter(dataHolder)
        mRecyclerview.adapter = adapter
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}