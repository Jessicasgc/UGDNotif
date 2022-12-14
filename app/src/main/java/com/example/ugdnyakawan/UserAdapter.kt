package com.example.ugdnyakawan

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ugdnyakawan.room.User
import kotlinx.android.synthetic.main.adapter_user.view.*

class UserAdapter (private val users: ArrayList<User>, private val
listener: OnAdapterListener) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_user,parent, false)
        )
    }
    override fun onBindViewHolder(holder: UserViewHolder, position:
    Int) {
        val user = users[position]
        holder.view.text_title.text = user.username
        holder.view.text_title.setOnClickListener{
            listener.onClick(user)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate(user)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(user)
        }
    }
    override fun getItemCount() = users.size
    inner class UserViewHolder( val view: View) :
        RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<User>){
        users.clear()
        users.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onClick(user: User)
        fun onUpdate(user: User)
        fun onDelete(user: User)
    }
}