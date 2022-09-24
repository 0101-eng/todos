package com.example.todos.update

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.todos.R
import com.example.todos.clearTime
import com.example.todos.database.MyDataBase
import com.example.todos.database.model.Todo
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class UpdateTodo : AppCompatActivity() {
    lateinit var update_btn : Button;
    lateinit var  date : TextView
    lateinit var title: EditText
    lateinit var  desc : EditText
    lateinit var titleLayout: TextInputLayout
    lateinit var detailsLayout: TextInputLayout
    lateinit var task : Todo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_todo)
        initViews()
        receiveData()
    }
    private fun receiveData() {
        task = (intent.getSerializableExtra("todo") as? Todo)!!
        title.setText(task.name)
        desc.setText(task.details)
        date.setText(task.date.toString())


    }

    private fun initViews() {
        update_btn = findViewById(R.id.update)
        title = findViewById(R.id.update_todo_title)
        desc = findViewById(R.id.update_todo_details)
        date = findViewById(R.id.update_chooseDate)
        titleLayout = findViewById(R.id.update_title_layout)
        detailsLayout = findViewById(R.id.update_details_layout)

        date.setOnClickListener(View.OnClickListener {
            showDatePicker()
        })
        update_btn.setOnClickListener(View.OnClickListener {
            var isValid:Boolean = validateForm()
            if(isValid){
                //update
                var title : String = title.text.toString()
                var desc : String = desc.text.toString()
                var date = calendar.clearTime().time
                var updatedTodo = Todo(task.id,title,desc,date,false)
                updateTodo(updatedTodo)

            }
        })


    }

    var calendar = Calendar.getInstance()


    fun showDatePicker() {

        val datePicker = DatePickerDialog(
            this,
            { p0, year, month, day ->
                calendar.set(Calendar.DAY_OF_MONTH, day)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.YEAR, year)
                date.setText("" + day + "/" + (month + 1) + "/" + year)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()

    }



    private fun updateTodo(todo : Todo) {
        MyDataBase.getInstance(this).todoDao().updateTodo(todo)
        Toast.makeText(this,"note updated", Toast.LENGTH_LONG).show()
        onBackPressed()
    }


    fun validateForm(): Boolean {
        var isValid = true;
        if (titleLayout.editText?.text.toString().isBlank()) {
            titleLayout.error = "please enter todo title";
            isValid = false;
        } else {
            titleLayout.error = null
        }
        if (detailsLayout.editText?.text.toString().isBlank()) {
            detailsLayout.error = "please enter todo details";
            isValid = false;
        } else {
            detailsLayout.error = null
        }
        return isValid;
    }
}