package com.example.myyoutube.presentation.player

import android.content.Context
import android.net.Uri
import com.example.myyoutube.R
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class PlayerHolder(
    val context: Context,
    val playerState: PlayerState,
    val url: String
) {

    val player: ExoPlayer
    private var playerView: PlayerView? = null

    init {
        player = SimpleExoPlayer.Builder(context)
            .setTrackSelector(DefaultTrackSelector(context))
            .setLoadControl(DefaultLoadControl())
            .build()
    }

    fun setPlayerView(view: PlayerView) {
        playerView?.player = null
        playerView = view
        playerView?.player = player
    }

    fun start() {
        val userAgent = Util.getUserAgent(context, context.getString(R.string.app_name))
        val dataSourceFactory = DefaultDataSourceFactory(context, userAgent)

        val mediaItem = MediaItem.fromUri(Uri.parse(url))

        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        player.setMediaSource(mediaSource)

        with(playerState) {
            player.playWhenReady = whenReady
            player.seekTo(window, position)
        }

        player.prepare()
    }

    fun stop() {
        with(playerState) {
            position = player.currentPosition
            window = player.currentWindowIndex
            whenReady = player.playWhenReady
        }

        player.stop()
        player.clearMediaItems()
    }

    fun release() {
        with(playerState) {
            position = player.currentPosition
            window = player.currentWindowIndex
            whenReady = player.playWhenReady
        }

        player.release()
        playerView?.player = null
    }
}