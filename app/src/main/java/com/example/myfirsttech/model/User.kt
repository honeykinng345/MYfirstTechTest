package com.example.myfirsttech.model

data class User(
    val login: String,
    val html_url: String,
    val avatar_url: String,
    val message: String
)

data class GitHubSearchResponse(val items: List<User>)