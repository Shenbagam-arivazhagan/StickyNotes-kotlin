package com.training.stickynotes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "StickyNoteDatabase"
        private val TABLE_NAME = "StickyNoteTable"
        private val KEY_ID = "id"
        private val KEY_TITLE = "title"
        private val KEY_NOTE = "content"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_NOTE + " TEXT" + ")")
        p0?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(p0)
    }


    fun addNewNote(note: StickyNoteModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, note.serialNo)
        contentValues.put(KEY_TITLE, note.title)
        contentValues.put(KEY_NOTE,note.notes )
        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }
    fun viewStickyNotes():List<StickyNoteModelClass>{
        val noteList:ArrayList<StickyNoteModelClass> = ArrayList<StickyNoteModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var serialNo: Int
        var title: String
        var notes: String
        if (cursor.moveToFirst()) {
            do {
                serialNo = cursor.getInt(cursor.getColumnIndex("id"))
                title = cursor.getString(cursor.getColumnIndex("title"))
                notes = cursor.getString(cursor.getColumnIndex("content"))
                val stickyNote= StickyNoteModelClass(serialNo = serialNo, title = title, notes = notes)
                noteList.add(stickyNote)
            } while (cursor.moveToNext())
        }
        return noteList
    }

    fun getDBCount() : Int{
        val selectQuery = "SELECT  * FROM $TABLE_NAME  order by id desc limit 1"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        cursor = db.rawQuery(selectQuery, null)
        var serialNo = 0
        if (cursor.moveToFirst()) {
            serialNo = cursor.getInt(cursor.getColumnIndex("id"))
        }else{
            println("cursors null")
        }
        return serialNo
    }
    fun deleteNote(serialNo: Int):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, serialNo)
        val success = db.delete(TABLE_NAME,"id="+serialNo,null)
        db.close() // Closing database connection
        return success
    }

    fun viewSelectedStickyNotes(serialNo: Int):List<StickyNoteModelClass>{
        val noteList:ArrayList<StickyNoteModelClass> = ArrayList<StickyNoteModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE id = $serialNo"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var serialNo: Int
        var title: String
        var notes: String
        if (cursor.moveToFirst()) {
            do {
                serialNo = cursor.getInt(cursor.getColumnIndex("id"))
                title = cursor.getString(cursor.getColumnIndex("title"))
                notes = cursor.getString(cursor.getColumnIndex("content"))
                val stickyNote= StickyNoteModelClass(serialNo = serialNo, title = title, notes = notes)
                noteList.add(stickyNote)
            } while (cursor.moveToNext())
        }
        return noteList
    }

    fun updateStickyNote(note: StickyNoteModelClass):Int{
        println("updateStickyNote --->")
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, note.serialNo)
        contentValues.put(KEY_TITLE, note.title)
        contentValues.put(KEY_NOTE,note.notes )
        val success = db.update(TABLE_NAME, contentValues,"id="+note.serialNo,null)
        db.close() // Closing database connection
        return success
    }

}