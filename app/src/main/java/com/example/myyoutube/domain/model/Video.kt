package com.example.myyoutube.domain.model

import java.io.Serializable

class Video : Serializable {
    var title: String? = null
    var videoUrl: String? = null
    var thumbnailUrl: String? = null
    var chatKey: String? = null

    // Empty constructor needed for Firestore serialization
    constructor()

    constructor(title: String?, videoUrl: String?, thumbnailUrl: String?, chatKey: String?) {
        this.title = title
        this.videoUrl = videoUrl
        this.thumbnailUrl = thumbnailUrl
        this.chatKey = chatKey
    }
}