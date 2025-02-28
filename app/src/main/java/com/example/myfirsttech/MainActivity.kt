package com.example.myfirsttech

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button

import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels
import com.example.myfirsttech.adapter.UserAdapter

import com.example.myfirsttech.viewmodel.UserViewModel


class MainActivity : AppCompatActivity() {

    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapter: UserAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchButton: Button
    private lateinit var noDataFound: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        recyclerView = findViewById(R.id.usersRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        progressBar = findViewById(R.id.progressBar)
        searchButton = findViewById(R.id.searchButton)
        noDataFound = findViewById(R.id.noDataFound)

        adapter = UserAdapter(mutableListOf())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchUsers(query)
            }
        }


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isNotEmpty()) {
                        noDataFound.visibility = View.GONE

                        //       adapter.removeUsers()
                        //    viewModel.searchUsers(it.toString())
                    } else {
                        if (it.isEmpty()) {
                            adapter.removeUsers()
                            noDataFound.visibility = View.VISIBLE
                            noDataFound.text = getResources().getString(R.string.please_enter_at_least_2_characters)
                        }
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.users.observe(this) { users ->
            if (users.isNotEmpty()) {
                noDataFound.visibility = View.GONE
                adapter.removeUsers()
                adapter.addUsers(users)
            } else {
                adapter.removeUsers()
                noDataFound.visibility = View.VISIBLE
                noDataFound.text =  getResources().getString(R.string.no_data_found)
            }
        }



        viewModel.error.observe(this) { error ->
            // Handle error (e.g., show a toast)
            Toast.makeText(applicationContext, "error:$error", Toast.LENGTH_LONG).show()
        }

        viewModel.isFetchAPi.observe(this) { isShowLoading ->
            // Handle error (e.g., show a toast)
            noDataFound.visibility = View.GONE
            if (isShowLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    if (adapter.itemCount > 0)
                        viewModel.loadMoreUsers()
                }
            }
        })
    }
}