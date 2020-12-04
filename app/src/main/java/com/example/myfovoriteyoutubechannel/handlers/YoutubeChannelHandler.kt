package com.example.myfovoriteyoutubechannel.handlers

import com.example.myfovoriteyoutubechannel.models.YoutubeChannel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class YoutubeChannelHandler {
     var database: FirebaseDatabase
     var youtubechannelRef: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        youtubechannelRef = database.getReference("youtubechannels")
    }

    fun create(youtubechannel: YoutubeChannel): Boolean {
        val id = youtubechannelRef.push().key
        youtubechannel.id = id

        youtubechannelRef.child(id!!).setValue(youtubechannel)
        return true
    }
    fun update(youtubechannel: YoutubeChannel): Boolean{
        youtubechannelRef.child(youtubechannel.id!!).setValue(youtubechannel)
        return true
    }
    fun delete(youtubechannel: YoutubeChannel): Boolean{
        youtubechannelRef.child(youtubechannel.id!!).removeValue()
        return true
    }

}