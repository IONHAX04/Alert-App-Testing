package com.ionhax.androidalertapplication

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbManager(context: Context) : SQLiteOpenHelper(context, dbname, null, 1) {

    companion object {
        private const val dbname = "reminder"
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val query =
            "create table tbl_reminder(id integer primary key autoincrement,title text,date text,time text)"
        sqLiteDatabase.execSQL(query)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        val query = "DROP TABLE IF EXISTS tbl_reminder"
        sqLiteDatabase.execSQL(query)
        onCreate(sqLiteDatabase)
    }

    fun addReminder(title: String, date: String, time: String): String {
        val database = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("title", title)
            put("date", date)
            put("time", time)
        }
        val result = database.insert("tbl_reminder", null, contentValues)
        return if (result == (-1).toLong()) {
            "Failed"
        } else {
            "Successfully inserted"
        }
    }

    fun readAllReminders(): Cursor {
        val database = this.readableDatabase
        val query = "select * from tbl_reminder order by id desc"
        return database.rawQuery(query, null)
    }
}