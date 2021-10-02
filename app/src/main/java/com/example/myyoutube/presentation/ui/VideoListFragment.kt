package com.example.myyoutube.presentation.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myyoutube.MainActivity
import com.example.myyoutube.databinding.FragmentVideoListBinding
import com.example.myyoutube.domain.model.Video
import com.example.myyoutube.presentation.ui.adapters.VideoAdapter
import com.example.myyoutube.presentation.viewmodels.VideoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoListFragment : Fragment(), VideoAdapter.Listener {

    private var _binding: FragmentVideoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoListViewModel by viewModels()
    private val currentVideos: ArrayList<Video> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVideoListBinding.inflate(inflater, container, false)
        val view = binding.root

        viewModel.videos.observe(viewLifecycleOwner, videosObserver)
        binding.videosRecyclerView.adapter = VideoAdapter(currentVideos, this)
        binding.videosRecyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        (activity as MainActivity).supportActionBar?.show()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val videosObserver = object : Observer<List<Video>> {
        @SuppressLint("NotifyDataSetChanged")
        override fun onChanged(newChats: List<Video>?) {
            newChats?.let {
                currentVideos.clear()
                currentVideos.addAll(it)
                binding.videosRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onVideoClick(position: Int) {
        val action = VideoListFragmentDirections.openChatAction(currentVideos[position])
        findNavController().navigate(action)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        binding.videosRecyclerView.adapter = VideoAdapter(currentVideos, this)

        val spanCount = if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) 1 else 2

        binding.videosRecyclerView.layoutManager = GridLayoutManager(requireContext(), spanCount)
    }
}