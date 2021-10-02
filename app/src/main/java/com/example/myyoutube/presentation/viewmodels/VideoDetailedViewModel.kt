package com.example.myyoutube.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.example.myyoutube.common.State
import com.example.myyoutube.domain.model.Comment
import com.example.myyoutube.domain.model.Video
import com.example.myyoutube.domain.repository.Repository
import com.example.myyoutube.presentation.player.PlayerHolder
import com.example.myyoutube.presentation.player.PlayerState
import com.google.android.exoplayer2.ui.PlayerView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoDetailedViewModel @Inject constructor(
    private val repository: Repository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>> = _comments

    val video = state.get<Video>("video")

    private val playerState: PlayerState = PlayerState()
    private var playerHolder: PlayerHolder? = null

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun init(context: Context) {
        viewModelScope.launch {
            if (video == null) return@launch
            repository.getComments(video.chatKey!!).collect {
                when (it) {
                    is State.Success -> {
                        _comments.value = it.data
                        _isLoading.value = false
                    }
                    is State.Loading -> {
                        _isLoading.value = true
                    }
                    is State.Failed -> {
                        _isLoading.value = false
                    }
                }
            }
        }
        playerHolder = PlayerHolder(context, playerState, video?.videoUrl ?: "")
    }

    fun sendComment(text: String) {
        val userName = repository.getUserName()
        val userPhotoUrl = repository.getUserPhotoUrl()
        val comment = Comment(text, userName, userPhotoUrl)
        repository.sendComment(comment, video?.chatKey)
    }

    fun setPlayerView(playerView: PlayerView) {
        playerHolder?.setPlayerView(playerView)
    }

    fun startPlayer() {
        playerHolder?.start()
    }

    fun stopPlayer() {
        playerHolder?.stop()
    }

    fun releasePlayer() {
        playerHolder?.release()
    }
}