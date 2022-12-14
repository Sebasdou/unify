package com.ucc.unify.ui.view.home.blogs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ucc.unify.data.model.Reply
import com.ucc.unify.data.model.Post
import com.ucc.unify.databinding.ItemBlogBinding

class PublicationsAdapter(
    private val showComments: (List<Reply>) -> Unit,
): ListAdapter<Post, PublicationsAdapter.PublicationVH>(Companion) {
    companion object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationVH =
        PublicationVH(ItemBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PublicationVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PublicationVH(private val binding: ItemBlogBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(post: Post){
            with(binding){
                tvPublication.text = post.publication
                tvDatetime.text = post.date.toString()
                tvNameUser.text = post.user.name
                btnComment.setOnClickListener { showComments(post.replies) }
            }
        }
    }
}
