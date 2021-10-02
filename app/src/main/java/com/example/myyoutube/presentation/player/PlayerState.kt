package com.example.myyoutube.presentation.player

import java.io.Serializable

data class PlayerState(
    var window: Int = 0,
    var position: Long = 0,
    var whenReady: Boolean = true
) : Serializable