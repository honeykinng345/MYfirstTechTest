package com.example.myfirsttech.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myfirsttech.model.User
import com.example.myfirsttech.R

class UserAdapter(private val users: MutableList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = users.size

    fun addUsers(newUsers: List<User>) {
        users.addAll(newUsers)
        notifyDataSetChanged()
    }
    fun removeUsers() {
        users.clear()
        notifyDataSetChanged()

    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userNameTextView: TextView = itemView.findViewById(R.id.userNameTextView)
        private val userUrlTextView: TextView = itemView.findViewById(R.id.userUrlTextView)
        private val userUrlImageView : ImageView = itemView.findViewById(R.id.imageAvatar)

        fun bind(user: User) {
            userNameTextView.text = user.login
            userUrlTextView.text = user.html_url
            Glide.with(itemView.context)
                .load(user.avatar_url)
                .apply(RequestOptions.circleCropTransform()) // Make it circular
                .placeholder(R.drawable.baseline_account_circle_24) // Optional
                .error(R.drawable.baseline_error_outline_24) // Optional
                .into(userUrlImageView)

        }
    }
}