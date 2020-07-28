package com.kelaspemula.submission1.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kelaspemula.submission1.R
import com.kelaspemula.submission1.Model.User
import kotlinx.android.synthetic.main.item_list.view.*

class UserAdapter: RecyclerView.Adapter<UserAdapter.UserViewHolder>(){
    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val data = ArrayList<User>()

    fun setData(item: ArrayList<User>) {
        data.clear()
        data.addAll(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(users : User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(users.photo)
                    .into(img_user)
                tv_name.text = users.name
                tv_type.text = users.type

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(users) }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}