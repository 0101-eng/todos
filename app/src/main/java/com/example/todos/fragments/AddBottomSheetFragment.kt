package com.example.todos.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import com.example.todos.R
import com.example.todos.clearTime
import com.example.todos.database.MyDataBase
import com.example.todos.database.model.Todo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import java.util.*


class AddBottomSheetFragment : BottomSheetDialogFragment() {
    lateinit var titletextInputLayout: TextInputLayout
    lateinit var descriptiontextInputLayout: TextInputLayout
    lateinit var addButton: Button
    lateinit var selectedDareButton: Button
    var selectedDate = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        titletextInputLayout = view.findViewById(R.id.title_edit_text)
        descriptiontextInputLayout = view.findViewById(R.id.description_edit_text)
        addButton = view.findViewById(R.id.add_button)
        selectedDareButton = view.findViewById(R.id.selected_date_btn)
        selectedDareButton.text =
            "${selectedDate.get(Calendar.YEAR)}/${selectedDate.get(Calendar.MONTH) + 1}/${
                selectedDate.get(Calendar.DAY_OF_MONTH)
            }"

        selectedDareButton.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(),
                { p0, year, month, day ->
                    selectedDate.set(Calendar.YEAR, year)
                    selectedDate.set(Calendar.MONTH, month)
                    selectedDate.set(Calendar.DAY_OF_MONTH, day)
                    selectedDareButton.text =
                        "${selectedDate.get(Calendar.YEAR)}/${selectedDate.get(Calendar.MONTH) + 1}/${
                            selectedDate.get(Calendar.DAY_OF_MONTH)
                        }"
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        addButton.setOnClickListener {
            if (!validate()) return@setOnClickListener
            selectedDate.clearTime()
            val todo= Todo(name= titletextInputLayout.editText?.text.toString(), details =descriptiontextInputLayout.editText?.text.toString(), isDone = false,
            date=selectedDate.time)
            MyDataBase.getInstance(requireActivity().applicationContext).todoDao()
                .addTodo(todo)
            onTodoCompleted?.onAddClicked()
            dismiss()
onAddClicked?.onAddClicked()
        }
    }
    var onAddClicked:OnAddClicked?=null
    interface OnAddClicked{
        fun onAddClicked()
    }
        fun validate(): Boolean {
            if (titletextInputLayout.editText?.text.toString().isEmpty()) {
                titletextInputLayout.error = "please enter valid title"
                return false
            } else {
                titletextInputLayout.error = null
            }
            if (descriptiontextInputLayout.editText?.text.toString().isEmpty()) {
                descriptiontextInputLayout.error = "please enter valid description"
                return false
            } else {
                descriptiontextInputLayout.error = null
            }
            return true
        }
    var onTodoCompleted:OnTodoCompleted?=null
interface OnTodoCompleted{
    fun onAddClicked()
}
    }



