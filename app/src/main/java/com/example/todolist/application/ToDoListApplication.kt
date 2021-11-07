package com.example.todolist.application

import android.app.Application
import com.example.todolist.helpers.HelperDB

class ToDoListApplication: Application()    {


    var helperDB: HelperDB? = null
        private set

    companion object {
        lateinit var instance: ToDoListApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(context = this)

    }
}