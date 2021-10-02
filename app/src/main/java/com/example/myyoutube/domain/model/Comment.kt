package com.example.myyoutube.domain.model

class Comment {
    var text: String? = null
    var name: String? = null
    var photoUrl: String? = null

    // Empty constructor needed for Firestore serialization
    constructor()

    constructor(text: String?, name: String?, photoUrl: String?) {
        this.text = text
        this.name = name
        this.photoUrl = photoUrl
    }
}