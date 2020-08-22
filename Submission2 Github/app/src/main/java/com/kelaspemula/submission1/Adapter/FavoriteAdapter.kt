package com.kelaspemula.submission1.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kelaspemula.submission1.Model.User
import com.kelaspemula.submission1.R
import kotlinx.android.synthetic.main.item_list.view.*

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    var mData = ArrayList<User>()
    set(value) {
        if (mData.size > 0 ) {
            this.mData.clear()
        }
        this.mData.addAll(mData)
        notifyDataSetChanged()
        field = value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(users: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(users.photo).apply {
                        RequestOptions().placeholder(R.drawable.ic_error)
                            .error(R.drawable.ic_error)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                    }
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