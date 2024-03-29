package com.example.todolist.extensions

import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import java.text.SimpleDateFormat as SimpleDateFormat
private val locale = Locale("pt", "BR")

fun Date.format(): String{
    return SimpleDateFormat ("dd/MM/yyyy", locale).format(this)
}

var TextInputLayout.text : String
    get() = editText?.text?.toString() ?: ""
    set(value){
        editText?.setText(value)
    }