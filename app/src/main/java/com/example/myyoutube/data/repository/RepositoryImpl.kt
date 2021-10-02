package com.example.myyoutube.data.repository

import android.content.Context
import com.example.myyoutube.common.State
import com.example.myyoutube.domain.model.Comment
import com.example.myyoutube.domain.model.Video
import com.example.myyoutube.domain.repository.Repository
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
class RepositoryImpl(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseAuth: FirebaseAuth
) : Repository {

    override fun getVideos(): Flow<State<List<Video>>> = callbackFlow {
        this@callbackFlow.sendBlocking(State.loading())

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val videos = snapshot.children.map {
                    it.getValue(Video::class.java)
                }
                this@callbackFlow.sendBlocking(State.success(videos.filterNotNull()))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(State.failed(error.toString()))
            }
        }

        firebaseDatabase.getReference(VIDEOS_REFERENCE)
            .addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase.getReference(VIDEOS_REFERENCE)
                .removeEventListener(postListener)
        }
    }

    override fun getComments(chatKey: String): Flow<State<List<Comment>>> = callbackFlow {
        this@callbackFlow.sendBlocking(State.loading())

        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val comments = snapshot.children.map {
                    it.getValue(Comment::class.java)
                }
                this@callbackFlow.sendBlocking(State.success(comments.filterNotNull()))
            }

            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.sendBlocking(State.failed(error.toString()))
            }
        }

        firebaseDatabase.getReference("$COMMENTS_REFERENCE/$chatKey")
            .addValueEventListener(postListener)

        awaitClose {
            firebaseDatabase.getReference("$COMMENTS_REFERENCE/$chatKey")
                .removeEventListener(postListener)
        }
    }

    override fun sendComment(comment: Comment, chatKey: String?) {
        firebaseDatabase.getReference("$COMMENTS_REFERENCE/$chatKey").push().setValue(comment)
    }

    override fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun signOut(context: Context) {
        AuthUI.getInstance().signOut(context)
    }

    override fun getUserName(): String {
        val user = firebaseAuth.currentUser
        return if (user != null) {
            user.displayName!!
        } else ANONYMOUS
    }

    override fun getUserPhotoUrl(): String? {
        val user = firebaseAuth.currentUser
        return user?.photoUrl?.toString()
    }

    companion object {
        private const val VIDEOS_REFERENCE = "image_chats"
        private const val COMMENTS_REFERENCE = "comments"
        private const val ANONYMOUS = "anonymous"
    }
}