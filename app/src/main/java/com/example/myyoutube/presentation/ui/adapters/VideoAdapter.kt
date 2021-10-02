package com.example.myyoutube.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myyoutube.R
import com.example.myyoutube.databinding.VideoItemBinding
import com.example.myyoutube.domain.model.Video

class VideoAdapter(private val videos: List<Video>, private val listener: Listener)
    : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    interface Listener {
        fun onVideoClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.video_item, parent, false)
        val binding = VideoItemBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(videos[position], listener, position)
    }

    override fun getItemCount() = videos.size

    inner class ViewHolder(private val binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Video, listener: Listener, position: Int) {
            loadImageIntoView(binding.thumbnailImageView, item.thumbnailUrl!!)
            binding.titleTextView.text = item.title
            binding.root.setOnClickListener { listener.onVideoClick(position) }
        }

        private fun loadImageIntoView(view: ImageView, url: String) {
            Glide.with(view.context).load(url).into(view)
        }
    }
}