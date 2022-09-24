package com.example.todos.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.todos.R
import com.example.todos.TodosAdapter

import com.example.todos.clearTime
import com.example.todos.database.MyDataBase
import com.example.todos.database.model.Todo
import com.example.todos.update.UpdateTodo

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.*




class ListFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var calendarView: MaterialCalendarView
    val adapter = TodosAdapter(null);


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews();

    }


    override fun onStart() {
        super.onStart()//once fragment is visible
    }

    override fun onResume() {
        super.onResume()
        // once user can interact
        getTodosListFromDB();
        // makeTouchHelper()
    }

    var calendar = Calendar.getInstance();
    lateinit var todosList:MutableList<Todo>

    fun getTodosListFromDB() {
        //  if(context==null)return;
        todosList = MyDataBase.getInstance(requireContext())
            .todoDao()
            .getTodosByDate(calendar.clearTime().time);// calendar.time (returns time in millisecond)
        adapter.changeData(todosList.toMutableList())
    }

    private fun initViews() {
        recyclerView = requireView().findViewById(R.id.todos_recyclerView)
        calendarView = requireView().findViewById(R.id.calendar_View)
        calendarView.selectedDate = CalendarDay.today()
        recyclerView.adapter = adapter
        onItemClicked()
        calendarView.setOnDateChangedListener { widget, calendarDay, selected ->
            Log.e("calendar day", "" + calendarDay.month);
            Log.e("calendar inside class", "" + calendar.get(Calendar.MONTH));

            calendar.set(Calendar.DAY_OF_MONTH, calendarDay.day)
            calendar.set(Calendar.MONTH, calendarDay.month - 1)
            calendar.set(Calendar.YEAR, calendarDay.year)
            getTodosListFromDB();
        }
    }

    fun onItemClicked(){

        adapter.onItemClickedToBeUpdated = object : TodosAdapter.OnItemClicked{
            override fun onItemClicked(todo: Todo) {

                showMessage(" what you need to do ?",
                    "update?",
                    DialogInterface.OnClickListener { dialog, which ->  moveToUpdate(todo)},
                    "make it Done!",
                    DialogInterface.OnClickListener { dialog, which -> makeTodoDone(todo) },
                    true
                )

            }

            override fun onItemClickedToBeDeleted(position: Int, todo: Todo) {
                MyDataBase.getInstance(requireContext()).todoDao().deleteTodo(todo)
                todosList.removeAt(position)
                adapter.notifyItemRemoved(position)
                adapter.notifyDataSetChanged()
                var l =  MyDataBase.getInstance(requireContext())
                    .todoDao()
                    .getTodosByDate(calendar.clearTime().time);// calendar.time (returns time in millisecond)
                adapter.changeData(todosList.toMutableList())
                adapter.changeData(l)
                Toast.makeText(requireContext(), "item removed", Toast.LENGTH_LONG).show()
            }
        }

    }




    private fun makeTodoDone(todo: Todo) {

        var newTodo = todo
        newTodo.isDone=true
        MyDataBase.getInstance(requireContext()).todoDao().updateTodo(newTodo)
        refreshTodosList()
        Toast.makeText(requireContext(), "done", Toast.LENGTH_LONG).show()
    }

    private fun refreshTodosList() {
        val list = MyDataBase.getInstance(requireContext()).todoDao().getTodosByDate(calendar.clearTime().time)
        adapter.changeData(list)
    }

    private fun moveToUpdate(todo:Todo) {

        var intent : Intent = Intent(requireContext(), UpdateTodo::class.java)
        intent.putExtra("todo", todo)
        startActivity(intent)
    }

    fun showMessage(
        message: String,
        posActionName: String,
        onPosClick: DialogInterface.OnClickListener?,
        negativeText: String,
        onNegativeClick: DialogInterface.OnClickListener?,
        isCancelable: Boolean
    ): AlertDialog? {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
        builder.setPositiveButton(posActionName, onPosClick)
        builder.setNegativeButton(negativeText, onNegativeClick)
        builder.setCancelable(isCancelable)
        return builder.show()
    }


            }


