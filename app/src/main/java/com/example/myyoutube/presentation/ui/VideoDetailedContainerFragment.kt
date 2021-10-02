package com.example.myyoutube.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.myyoutube.R
import com.example.myyoutube.databinding.FragmentVideoDetailedContainerBinding
import com.example.myyoutube.domain.model.Video
import com.example.myyoutube.presentation.viewmodels.VideoDetailedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoDetailedContainerFragment : Fragment() {

    private var _binding: FragmentVideoDetailedContainerBinding? = null
    private val binding get() = _binding!!

    private val args: VideoDetailedFragmentArgs by navArgs()
    private var video: Video? = null

    private val viewModel: VideoDetailedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoDetailedContainerBinding.inflate(inflater, container, false)
        val view = binding.root

        video = args.video

        // trigger lazy initialization of viewModel
        viewModel.init(requireContext())

        childFragmentManager.commit {
            replace<VideoDetailedFragment>(R.id.videoDetailedFragmentContainerView)
            setReorderingAllowed(true)
        }

        return view
    }
}