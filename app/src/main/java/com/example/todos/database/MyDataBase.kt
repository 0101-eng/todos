package com.example.todos.database

import android.content.Context
import com.example.todos.database.model.Todo

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Todo::class], version = 1)
@TypeConverters(DateConverter::class)
abstract class MyDataBase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        private val DATABASE_NAME = "todo-Database";

        private var myDataBase: MyDataBase? = null;

        fun getInstance(context: Context): MyDataBase {
            // single object from database (Singleton pattern)
            if (myDataBase == null) {
                myDataBase = Room.databaseBuilder(
                    context, MyDataBase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()//ANR -> wait-kill
                    .build()
            }
            return myDataBase!!;
        }
    }

}