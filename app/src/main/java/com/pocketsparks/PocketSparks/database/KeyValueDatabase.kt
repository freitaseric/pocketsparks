package com.pocketsparks.PocketSparks.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class KeyValueDatabase(context: Context, private val table: String, val version: Int = 1) :
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

    suspend fun set(key: String, value: Any): Long? = withContext(Dispatchers.IO) {
        val db = writableDatabase
        return@withContext try {
            db.beginTransaction()
            val values = ContentValues().apply {
                put(COLUMN_KEY, key)
                put(COLUMN_VALUE, value.toString())
            }
            val insertId =
                db.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE)
            db.setTransactionSuccessful()
            insertId
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    suspend fun getString(key: String): String? = withContext(Dispatchers.IO) {
        val db = readableDatabase
        return@withContext try {
            val cursor = readableDatabase.query(
                table, arrayOf(COLUMN_VALUE), "$COLUMN_KEY = ?", arrayOf(key), null, null, null
            )
            var value: String? = null
            cursor.use {
                when {
                    it.moveToFirst() -> {
                        value = it.getString(it.getColumnIndexOrThrow(COLUMN_VALUE))
                    }
                }
            }
            value
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            db.close()
        }
    }

    suspend fun getInt(key: String): Int? = withContext(Dispatchers.IO) {
        val db = readableDatabase
        return@withContext try {
            val cursor = readableDatabase.query(
                table, arrayOf(COLUMN_VALUE), "$COLUMN_KEY = ?", arrayOf(key), null, null, null
            )
            var value: Int? = null
            cursor.use {
                when {
                    it.moveToFirst() -> {
                        value = it.getInt(it.getColumnIndexOrThrow(COLUMN_VALUE))
                    }
                }
            }
            value
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            db.close()
        }
    }

    suspend fun getUri(key: String): Uri? = withContext(Dispatchers.IO) {
        val db = readableDatabase
        return@withContext try {
            val cursor = readableDatabase.query(
                table, arrayOf(COLUMN_VALUE), "$COLUMN_KEY = ?", arrayOf(key), null, null, null
            )
            var value: Uri? = null
            cursor.use {
                when {
                    it.moveToFirst() -> {
                        val valueAsString = it.getString(it.getColumnIndexOrThrow(COLUMN_VALUE))
                        value = Uri.parse(valueAsString)
                    }
                }
            }
            value
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            db.close()
        }
    }

    suspend fun delete(key: String): Int = withContext(Dispatchers.IO) {
        val db = writableDatabase
        return@withContext try {
            db.beginTransaction()
            val rowsAffected = db.delete(table, "$COLUMN_VALUE = ?", arrayOf(key))
            db.setTransactionSuccessful()
            rowsAffected
        } catch (e: Exception) {
            e.printStackTrace()
            0
        } finally {
            db.endTransaction()
            db.close()
        }
    }
}