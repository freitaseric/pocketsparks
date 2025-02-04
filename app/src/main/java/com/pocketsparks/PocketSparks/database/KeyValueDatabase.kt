package com.pocketsparks.PocketSparks.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import androidx.core.database.sqlite.transaction
import java.io.File

class KeyValueDatabase(context: Context, val table: String, val version: Int = 1) :
    SQLiteOpenHelper(context, "$table.db", null, version) {
    companion object {
        const val COLUMN_KEY = "key"
        const val COLUMN_VALUE = "value"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $table (
                $COLUMN_KEY TEXT PRIMARY KEY,
                $COLUMN_VALUE TEXT
            );
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            db.execSQL(
                """
                DROP TABLE IF EXISTS $table;
            """.trimIndent()
            )
            onCreate(db)
        }
    }

    fun set(key: String, value: Any) {
        val values = ContentValues().apply {
            put(COLUMN_KEY, key)
            put(COLUMN_VALUE, value.toString())
        }
        writableDatabase.transaction {
            writableDatabase.insertWithOnConflict(
                table, null, values, SQLiteDatabase.CONFLICT_REPLACE
            )
            writableDatabase.close()
        }
    }

    fun getString(key: String): String? {
        readableDatabase.transaction {
            val cursor = readableDatabase.query(
                table, arrayOf(COLUMN_VALUE), "$COLUMN_KEY = ?", arrayOf(key), null, null, null
            )
            var value: String? = null
            if (cursor.moveToFirst()) {
                value = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE))
            }
            cursor.close()
            readableDatabase.close()
            return value
        }
    }

    fun getInt(key: String): Int? {
        readableDatabase.transaction {
            val cursor = readableDatabase.query(
                table, arrayOf(COLUMN_VALUE), "$COLUMN_KEY = ?", arrayOf(key), null, null, null
            )
            var value: Int? = null
            if (cursor.moveToFirst()) {
                value = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_VALUE))
            }
            cursor.close()
            readableDatabase.close()
            return value
        }
    }

    fun getUri(key: String): Uri? {
        readableDatabase.transaction {
            val cursor = readableDatabase.query(
                table, arrayOf(COLUMN_VALUE), "$COLUMN_KEY = ?", arrayOf(key), null, null, null
            )
            var value: Uri? = null
            if (cursor.moveToFirst()) {
                val valueAsString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VALUE))
                value = Uri.fromFile(File(valueAsString))
            }
            cursor.close()
            readableDatabase.close()
            return value
        }
    }
}