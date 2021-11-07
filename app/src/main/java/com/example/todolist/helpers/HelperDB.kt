package com.example.todolist.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.todolist.datasource.TaskDataSource
import com.example.todolist.extensions.text
import com.example.todolist.model.Task
import com.example.todolist.ui.AddTaskActivity

class HelperDB(
    context: Context?,
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL) {

    companion object {
        private val NOME_BANCO = "todolist.db"
        private val VERSAO_ATUAL = 1
    }


    val TABLE_NAME = "todolist"
    val COLUMNS_ID = "id"
    val COLUMNS_TITLE = "title"
    val COLUMNS_DESCRIPTION = "description"
    val COLUMNS_DATE = "date"
    val COLUMNS_HOUR = "hour"


    private val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMNS_ID INTEGER NOT NULL, " +
            "$COLUMNS_TITLE TEXT NOT NULL, " +
            "$COLUMNS_DESCRIPTION TEXT, " +
            "$COLUMNS_DATE TEXT NOT NULL, " +
            "$COLUMNS_HOUR TEXT NOT NULL, " +
            "PRIMARY KEY ($COLUMNS_ID AUTOINCREMENT)" +
            ")"


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion) {
            // update your table or create a new table
            db?.execSQL(DROP_TABLE)
        }
        onCreate(db)

    }

    fun findById(taskId: String) : Task {
        val db = readableDatabase
        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_ID = ? "
        var array = arrayOf(taskId)
        var cursor = db.rawQuery(sql, array)


        if (cursor.count == 0){
            db.close()
            return Task(0, "","","","")
        }

        Log.e("cursor Count: ", cursor.count.toString())

        cursor.moveToFirst()
          var task = Task (
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)?: 0),
                cursor.getString(cursor.getColumnIndex(COLUMNS_TITLE)?: 1),
                cursor.getString(cursor.getColumnIndex(COLUMNS_HOUR)?: 2),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DATE)?: 3),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DESCRIPTION)?: 4)
            )
        db.close()
        return task
    }

    fun findToDoItem (findByDate: String) : List<Task>{
        val db = readableDatabase ?: return mutableListOf()
        val lista = mutableListOf<Task>()
        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_DATE LIKE ? ORDER BY $COLUMNS_DATE, $COLUMNS_HOUR ASC"
        var array = arrayOf("%$findByDate%")
        var cursor = db.rawQuery(sql, array)

        if (cursor == null) {
            db.close()
            return mutableListOf()
        }

        while ( cursor.moveToNext()){
            var task = Task (
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)?: 0),
                cursor.getString(cursor.getColumnIndex(COLUMNS_TITLE)?: 1),
                cursor.getString(cursor.getColumnIndex(COLUMNS_HOUR)?: 2),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DATE)?: 3),
                cursor.getString(cursor.getColumnIndex(COLUMNS_DESCRIPTION)?: 4)
            )
            lista.add(task)
        }
        db.close()
        return lista
    }

    fun insertTask(task: Task) {
        val db = writableDatabase ?: return
        if (task.id == 0) {
            val sql = "INSERT INTO $TABLE_NAME (" +
                    "$COLUMNS_TITLE, " +
                    "$COLUMNS_DATE," +
                    "$COLUMNS_HOUR," +
                    "$COLUMNS_DESCRIPTION) VALUES (?, ?, ?, ?)"

            var array = arrayOf(task.title, task.date, task.hour, task.description)

            db.execSQL(sql, array)
        } else {
            updateTask(task)
        }
        db.close()
    }

    fun deleteTask(id: Int){
        val db = writableDatabase?: return
        // Define 'where' part of query.
        val whereArgs = "$COLUMNS_ID = ?"
        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(id.toString())
        // Issue SQL statement.
        val deletedRows = db.delete(TABLE_NAME, whereArgs, selectionArgs)
        db.close()
    }

    fun updateTask(task: Task){
        val db = writableDatabase?: return

        val content = ContentValues()
        content.put(COLUMNS_TITLE, task.title)
        content.put(COLUMNS_DATE, task.date)
        content.put(COLUMNS_HOUR, task.hour)
        content.put(COLUMNS_DESCRIPTION, task.description)
        val where = "id = ?"
        var args = arrayOf("${task.id}")

        db.update(TABLE_NAME, content, where, args )
        db.close()

    }
}

