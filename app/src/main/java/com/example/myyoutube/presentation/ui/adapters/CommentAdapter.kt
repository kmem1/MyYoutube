package com.example.myyoutube.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutube.R
import com.example.myyoutube.databinding.CommentBinding
import com.example.myyoutube.domain.model.Comment

class CommentAdapter(private val comments: List<Comment>, private val listener: Listener? = null)
    : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    interface Listener {
        fun onCommentClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.comment, parent, false)
        val binding = CommentBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comments[position], listener, position)
    }

    override fun getItemCount() = comments.size

    inner class ViewHolder(private val binding: CommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Comment, listener: Listener?, position: Int) {

            if (item.photoUrl != null) {
                loadImageIntoView(binding.commentatorImageView, item.photoUrl!!)
            } else {
                binding.commentatorImageView.setImageResource(R.drawable.ic_account_circle_black_36dp)
            }

            binding.commentatorNameView.text = item.name
            binding.commentatorTextView.text = item.text
            binding.root.setOnClickListener { listener?.onCommentClick(position) }
        }

        private fun loadImageIntoView(view: ImageView, url: String) {
            Glide.with(view.context).load(url).into(view)
        }
    }
}