package com.example.myyoutube.presentation.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myyoutube.MainActivity
import com.example.myyoutube.R
import com.example.myyoutube.databinding.FragmentVideoFullscreenBinding
import com.example.myyoutube.presentation.viewmodels.VideoDetailedViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VideoFullscreenFragment : Fragment() {

    private var _binding: FragmentVideoFullscreenBinding? = null
    private val binding get() = _binding!!

    private val viewModel: VideoDetailedViewModel by viewModels( { requireParentFragment() } )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoFullscreenBinding.inflate(inflater, container, false)

        val view = binding.root

        val fullscreenButton = binding.playerView.findViewById<ImageView>(R.id.exo_fullscreen_icon)
        fullscreenButton.setOnClickListener(fullscreenButtonOnClickListener)

        return view
    }

    override fun onStart() {
        super.onStart()

        viewModel.setPlayerView(binding.playerView)
        binding.playerView.findViewById<ImageView>(R.id.exo_fullscreen_icon)
            .setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_fullscreen_close)
            )
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

    override fun onPause() {
        super.onPause()

        (activity as MainActivity).requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }


    private val fullscreenButtonOnClickListener = View.OnClickListener {
        parentFragmentManager.popBackStack()
    }

    companion object {
        const val TAG = "video_fullscreen_fragment"
    }
}