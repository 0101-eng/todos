package com.example.todos

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.todos.database.model.Todo

class TodosAdapter(var items: MutableList<Todo>?): RecyclerView.Adapter<TodosAdapter.ViewHolder>() {

    var onItemClickedToBeUpdated: OnItemClicked? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = items!!.get(position)
        holder.titleTextView.text = todo.name
        holder.descriptionTextView.text = todo.details
        //  if(onItemClickedToBeUpdated!=null){
        //     holder.itemView.findViewById<CardView>(R.id.card).setOnClickListener{
        //   onItemClickedToBeUpdated?.onItemClicked(items[position])
        //     }
        // }
        if (todo.isDone == true) {
            holder.done.setBackgroundResource(R.drawable.done_background)
            holder.titleTextView.setTextColor(Color.GREEN)
            holder.lineView.setBackgroundResource(R.drawable.done_background)

        }

        if (onItemClickedToBeUpdated != null) {
            holder.cardView.setOnClickListener(View.OnClickListener {
                onItemClickedToBeUpdated?.onItemClicked(todo)
            })

            holder.rightView.setOnClickListener(View.OnClickListener {
                onItemClickedToBeUpdated?.onItemClickedToBeDeleted(position, todo)
            })


        }


    }

    fun changeData(newItems: MutableList<Todo>) {
        items = newItems;
        notifyDataSetChanged();// notify adapter that data has been changed
    }

    override fun getItemCount(): Int = items?.size ?: 0;


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        val rightView: ImageView = itemView.findViewById(R.id.right_view)
        val cardView: CardView = itemView.findViewById(R.id.card)

        val done: ImageView = itemView.findViewById(R.id.mark_as_done)
        val lineView: View = itemView.findViewById(R.id.line_view)


    }

    interface OnItemClicked {
        fun onItemClicked(todo: Todo)
        fun onItemClickedToBeDeleted(position: Int, todo: Todo)


    }
}

