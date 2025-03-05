package com.example.apkprofile

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(private val context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object {
        //Tabel User
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "data"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        //Tabel Note
        private const val TABLE_NOTES = "allnotes"
        private const val COLUMN_NOTE_ID = "id"
        private const val COLUMN_NOTE_TITLE = "title"
        private const val COLUMN_NOTE_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT)")
        db?.execSQL(createTableQuery)

        val createNotesTable = """
        CREATE TABLE $TABLE_NOTES (
            $COLUMN_NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
            $COLUMN_NOTE_TITLE TEXT NOT NULL, 
            $COLUMN_NOTE_CONTENT TEXT NOT NULL
        )
        """.trimIndent()
        db?.execSQL(createNotesTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        onCreate(db)
    }

    fun insertUser(username: String, password: String): Long {
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val db = writableDatabase
        return db.insert(TABLE_NAME, null, values)
    }

    fun readUser(username: String, password: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val userExists = cursor.count > 0
        cursor.close()
        return userExists
    }

    fun insertNote(note: Note): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", note.title)
            put("content", note.content)
        }

        val result = db.insert("allnotes", null, values)

        if (result == -1L) {
            android.util.Log.e("DatabaseError", "Failed to insert note: Title=${note.title}, Content=${note.content}")
        } else {
            android.util.Log.i("DatabaseSuccess", "Note inserted successfully with ID $result")
        }

        db.close()
        return result != -1L
    }


    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NOTES", null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTE_ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_TITLE))
                    val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_CONTENT))
                    notes.add(Note(id, title, content))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }

        return notes
    }

    fun deleteNote(id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NOTES, "$COLUMN_NOTE_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    fun updateNote(note: Note): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOTE_TITLE, note.title)
            put(COLUMN_NOTE_CONTENT, note.content)
        }
        val result = db.update(TABLE_NOTES, values, "$COLUMN_NOTE_ID = ?", arrayOf(note.id.toString()))
        db.close()
        return result > 0
    }
}