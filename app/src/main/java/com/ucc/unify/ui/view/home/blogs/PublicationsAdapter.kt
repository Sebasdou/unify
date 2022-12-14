package com.ucc.unify.ui.view.home.blogs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ucc.unify.data.model.Comment
import com.ucc.unify.data.model.Publication
import com.ucc.unify.databinding.ItemBlogBinding

class PublicationsAdapter(
    private val showComments: (List<Comment>) -> Unit,
): ListAdapter<Publication, PublicationsAdapter.PublicationVH>(Companion) {
    companion object : DiffUtil.ItemCallback<Publication>() {
        override fun areItemsTheSame(oldItem: Publication, newItem: Publication): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Publication, newItem: Publication): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicationVH =
        PublicationVH(ItemBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: PublicationVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PublicationVH(private val binding: ItemBlogBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(publication: Publication){
            with(binding){
                tvPublication.text = publication.publication
                tvDatetime.text = publication.date.toString()
                tvNameUser.text = publication.user.name
                btnComment.setOnClickListener { showComments(publication.comments) }
            }
        }
    }
}
