package com.example.myfirsttech.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirsttech.model.User
import com.example.myfirsttech.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val repository = UserRepository()
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _isFetchAPi = MutableLiveData<Boolean>()
    val isFetchAPi: LiveData<Boolean> get() = _isFetchAPi

    private var currentPage = 1
    private var currentQuery = ""

    fun searchUsers(query: String) {
        currentQuery = query
        currentPage = 1
        fetchUsers()
    }

    fun loadMoreUsers() {
        currentPage++
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            try {
                _isFetchAPi.value = true
                val response = repository.searchUsers(currentQuery, currentPage, 30)
                if (response.isSuccessful) {
                    val newUsers = response.body()?.items ?: emptyList()
                    if (currentPage == 1) {
                        _users.value = newUsers
                    } else {
                        _users.value = _users.value?.plus(newUsers)
                    }
                    _isFetchAPi.value = false

                } else {
                    _isFetchAPi.value = false
                    if (response.code() == 403) {
                        _error.value = "API rate limit exceeded"

                    } else {
                        _error.value = "Error: ${response.message()}"

                    }

                }
            } catch (e: Exception) {
                _isFetchAPi.value = false
                _error.value = "Exception: ${e.message}"
            }
        }
    }
}