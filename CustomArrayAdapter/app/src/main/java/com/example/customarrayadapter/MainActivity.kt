package com.example.customarrayadapter

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var lvItems: ListView
    private lateinit var userAdapter: UsersAdapter
    private lateinit var userList: ArrayList<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lvItems = findViewById(R.id.lvItems)

        userList = arrayListOf(
            User("Harry", "San Diego"),
            User("Marla", "San Francisco"),
            User("Sarah", "San Marco")
        )

        userAdapter = UsersAdapter(this, userList)
        lvItems.adapter = userAdapter
    }
}