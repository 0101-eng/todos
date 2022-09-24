package com.example.todos

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todos.database.model.Todo
import com.example.todos.fragments.AddBottomSheetFragment
import com.example.todos.fragments.ListFragment
import com.example.todos.fragments.SettingsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.snackbar.Snackbar
import java.util.*

class HomeActivity : AppCompatActivity() {
    lateinit var bottomNavigation: BottomNavigationView
    lateinit var addButton: FloatingActionButton
    val todoListFragment = ListFragment();
    val settingsFragment = SettingsFragment();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        bottomNavigation = findViewById(R.id.navigation_view)
        addButton = findViewById(R.id.add)
        addButton.setOnClickListener {
            //   val intent = Intent(this,UpdateTodoActivity::class.java)
            //   startActivity(intent)
            showAddBottomSheet()
        }
        bottomNavigation.setOnItemSelectedListener(
            NavigationBarView.OnItemSelectedListener { item ->
                if (item.itemId == R.id.ic_list) {
                    pushFragment(todoListFragment)
                } else if (item.itemId == R.id.ic_settings) {
                    pushFragment(settingsFragment)
                }
                return@OnItemSelectedListener true;
            }
        )
        bottomNavigation.selectedItemId = R.id.ic_list

    }

    private fun showAddBottomSheet() {
        val addBottomSheet = AddBottomSheetFragment();
        addBottomSheet.show(supportFragmentManager, "");
        addBottomSheet.onAddClicked = object : AddBottomSheetFragment.OnAddClicked {
            override fun onAddClicked() {
                //refresh Todos List from database inside listFragment
                if (todoListFragment.isVisible)
                    todoListFragment.getTodosListFromDB();
            }
        }
    }

    fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }



}