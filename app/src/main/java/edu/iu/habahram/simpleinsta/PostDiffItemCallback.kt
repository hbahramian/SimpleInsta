package edu.iu.habahram.simpleinsta

import androidx.recyclerview.widget.DiffUtil
import edu.iu.habahram.simpleinsta.model.Post

class PostDiffItemCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post)
            = (oldItem.description == newItem.description)
    override fun areContentsTheSame(oldItem: Post, newItem: Post) = (oldItem == newItem)
}