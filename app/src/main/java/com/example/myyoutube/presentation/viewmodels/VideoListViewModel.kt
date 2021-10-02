package com.example.myyoutube.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myyoutube.common.State
import com.example.myyoutube.domain.model.Video
import com.example.myyoutube.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoListViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _videos = MutableLiveData<List<Video>>()
    val videos: LiveData<List<Video>> = _videos

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            repository.getVideos().collect {
                when (it) {
                    is State.Success -> {
                        _videos.value = it.data
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
    }
}