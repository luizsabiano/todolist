package com.example.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.application.ToDoListApplication
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.datasource.TaskDataSource
import com.example.todolist.model.Task

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(this)
        updateList()

        insertListeners()
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener{
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {

            val intent = Intent(this, AddTaskActivity::class.java)

            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }
        adapter.listenerDelete = {

            ToDoListApplication.instance.helperDB?.deleteTask(it.id)

            updateList()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK)  updateList()
    }

    private fun updateList() {

        binding.progress.visibility = View.VISIBLE

        // Executa a pesquisa no BD SQLite em uma thred secundária, deixando a thred principal livre para outras tarefas

        Thread(Runnable {
            var list: List<Task> = mutableListOf()
            try {
                list = ToDoListApplication.instance.helperDB?.findToDoItem("") ?: mutableListOf()
            } catch (ex: Exception){
                ex.printStackTrace()
            }

            // As atualização da view não podem ser executadas por uma thred secundária
            // Executa a atualização da view para uma thred UI (principal)
            runOnUiThread {
                adapter.submitList(list)
                binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) VISIBLE else GONE
                binding.progress.visibility = View.GONE
            }
        }).start()
    }

    companion object{
        private const val CREATE_NEW_TASK = 1000

    }

    /* App To do list

    * Uma aplicação de lista de tarefas simples onde foi implementado:
    *
    * Um recycleView, um adpter e um viewHolder.
    *
    * Os dados são persistidos em uma banco de dados SQLite - Utilizado o sqlHelper
    * Foi implementado um insert, update, delete e uma pesquisa.
    *
    * As pesquisa em banco de dados, inserções, deleções e atualizações foram implementadas em threds secundárias para não travar o aplicativo pricipal.
    * Acrescentado um layout loader para não parecer travado ao consultar bd, por causa da alta performance do bd provavelmente nunca vai aparecer.
    *
    *
    * Implementação CardView na view Task

    * Créditos:
    * Responsável pelo base do conhecimento:
    *  - Curso adquirido através da DIO - Digital Innovation One e financiado pelo Carrefour
    *  - persistência de dados, threds e loader -> Vinícius Fragelli - Everis
    *  - RecycleView, viewHolder, adpter, viewBinding e reciclagem em boas prática de implementação -> Ezequiel Messore - Koike Tecnologia
    *  - CardView, RecycleView, adpter, viewHolder -> Igor Ferrani
    *
    *
    * */
}