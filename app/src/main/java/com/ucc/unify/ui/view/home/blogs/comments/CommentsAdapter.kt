package com.ucc.unify.ui.view.home.blogs.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ucc.unify.data.model.Comment
import com.ucc.unify.databinding.ItemCommentBinding

class CommentsAdapter: ListAdapter<Comment, CommentsAdapter.CommentVH>(Companion) {
    companion object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentVH =
        CommentVH(ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CommentVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CommentVH(private val binding: ItemCommentBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(comment: Comment){
            with(binding){
                tvComment.text = comment.comment
                tvDatetimeR.text = comment.date
                tvNameUserR.text = comment.user.name
            }
        }
    }
}
