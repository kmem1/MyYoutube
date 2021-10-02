package com.example.myyoutube.presentation.ui

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myyoutube.MainActivity
import com.example.myyoutube.R
import com.example.myyoutube.databinding.FragmentVideoDetailedBinding
import com.example.myyoutube.domain.model.Comment
import com.example.myyoutube.domain.model.Video
import com.example.myyoutube.presentation.ui.adapters.CommentAdapter
import com.example.myyoutube.presentation.ui.observers.SendButtonObserver
import com.example.myyoutube.presentation.viewmodels.VideoDetailedViewModel
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoDetailedFragment : Fragment() {

    private var _binding: FragmentVideoDetailedBinding? = null
    private val binding get() = _binding!!

    private var video: Video? = null

    private val viewModel: VideoDetailedViewModel by viewModels({ requireParentFragment() })
    private val currentComments: ArrayList<Comment> = ArrayList()

    private var isFullscreen = false

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoDetailedBinding.inflate(inflater, container, false)
        val view = binding.root

        video = viewModel.video
        binding.titleView.text = video?.title

        viewModel.comments.observe(viewLifecycleOwner, commentsObserver)
        binding.commentsRecyclerView.adapter = CommentAdapter(currentComments)
        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.titleView.text = video?.title

        binding.commentEditText.addTextChangedListener(SendButtonObserver(binding.sendButton))

        binding.sendButton.setOnClickListener(sendButtonClickListener)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val fullscreenButton = binding.playerView.findViewById<ImageView>(R.id.exo_fullscreen_icon)
        fullscreenButton.setOnClickListener(fullscreenButtonOnClickListener)

        showSystemUI()

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


    override fun onStart() {
        super.onStart()

        viewModel.setPlayerView(binding.playerView)

        if (!isFullscreen && Util.SDK_INT >= 24) {
            viewModel.startPlayer()
        }

        (activity as MainActivity).supportActionBar?.hide()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()

        if (!isFullscreen && Util.SDK_INT < 24) {
            viewModel.startPlayer()
        }

        (activity as MainActivity).requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onPause() {
        super.onPause()

        if (!isFullscreen && Util.SDK_INT < 24) {
            viewModel.stopPlayer()
        }

        (activity as MainActivity).requestedOrientation =
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onStop() {
        super.onStop()

        if (!isFullscreen && Util.SDK_INT >= 24) {
            viewModel.stopPlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.releasePlayer()
    }

    @SuppressLint("NotifyDataSetChanged")
    private val commentsObserver = Observer<List<Comment>> {
        currentComments.clear()
        currentComments.addAll(it)
        binding.commentsRecyclerView.adapter?.notifyDataSetChanged()
        binding.commentsRecyclerView.scrollToPosition(currentComments.lastIndex)
    }

    private val sendButtonClickListener = View.OnClickListener {
        viewModel.sendComment(binding.commentEditText.text.toString().trim())
        binding.commentEditText.setText("")
        (activity as? MainActivity)?.hideKeyboard()
    }

    private val fullscreenButtonOnClickListener = View.OnClickListener {
        isFullscreen = true
        parentFragmentManager.commit {
            replace<VideoFullscreenFragment>(
                R.id.videoDetailedFragmentContainerView,
                VideoFullscreenFragment.TAG
            )
            setReorderingAllowed(true)
            addToBackStack(null)
        }
        hideSystemUI()
    }

    private fun hideSystemUI() {
        val mainActivity = activity as MainActivity
        with(mainActivity) {
            WindowCompat.setDecorFitsSystemWindows(this.window, false)
            WindowInsetsControllerCompat(this.window, this.mainContainer).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    private fun showSystemUI() {
        val mainActivity = activity as MainActivity
        WindowCompat.setDecorFitsSystemWindows(mainActivity.window, true)
        WindowInsetsControllerCompat(mainActivity.window, mainActivity.mainContainer)
            .show(WindowInsetsCompat.Type.systemBars())
    }
}