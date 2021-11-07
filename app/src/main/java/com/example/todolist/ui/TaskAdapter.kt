package com.example.todolist.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.model.Task
import com.example.todolist.databinding.ItemTaskBinding


// 1 - criar um classe Adapter extender/implementar de RecyclerView.Adapter<>
// 2 - criar uma inner class ViewHolder extender/implementar RecyclerView.ViewHolder()
// 3 - passar para nossa class ..ViewHolder() um itemView: View -> ..ViewHolder(itemView: View)
// 4 - repassá-lo para a RecyclerView.ViewHolder() -> RecyclerView.ViewHolder(itemView)
// 5 - passar para a classe RecyclerView.Adapter<> a inner class ViewHolder ->
//     RecyclerView.Adapter<YourAdapterClass.TaskAdapterViewHolder>()
// 6 - Implementar os membros da classe Adpter
// 7 - Criar uma variável que receberá uma lista : MutableList<YourModelClass> do tipo mutableListOf()
// 8 - Criar sua classe Modelo
// 9 - implmentar o retorno do getItemCount() --> return list.size
// 10 - implementar nosso adpter fun bind(modelClass: ModelClass)
// 11 - implementar o onBindViewHolder(..), responsável por popular o item na lista


class TaskAdapter: RecyclerView.Adapter<TaskAdapter.TaskAdapterViewHolder>() {

    private val list: MutableList<Task> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskAdapterViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(list: List<Task>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class TaskAdapterViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)
        private val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        private val tvDescription: TextView = itemView.findViewById(R.id.tv_description)

        fun bind(task: Task) {
            tvTitle.text = task.title
            tvDate.text = "${task.date} ${task.hour}"
            tvDescription.text = task.description

        }
    }



}