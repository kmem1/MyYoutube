package com.example.myyoutube.domain.repository

import android.content.Context
import com.example.myyoutube.common.State
import com.example.myyoutube.domain.model.Comment
import com.example.myyoutube.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getVideos(): Flow<State<List<Video>>>
    fun getComments(chatKey: String): Flow<State<List<Comment>>>
    fun sendComment(comment: Comment, chatKey: String?)

    fun isUserSignedIn(): Boolean
    fun signOut(context: Context)
    fun getUserName(): String
    fun getUserPhotoUrl(): String?
}