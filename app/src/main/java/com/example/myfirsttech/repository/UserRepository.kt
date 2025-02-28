package com.example.myfirsttech.repository

import com.example.myfirsttech.model.GitHubSearchResponse
import com.example.myfirsttech.network.RetrofitInstance
import retrofit2.Response

class UserRepository {
    suspend fun searchUsers(query: String, page: Int, perPage: Int): Response<GitHubSearchResponse> {
        return RetrofitInstance.service.searchUsers(query, page, perPage)
    }
}