package com.example.myfovoriteyoutubechannel.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class YoutubeChannel(var id: String? = "", var rank: String? = "", var name: String? = "", var link: String? = "", var reason: String? = "" ) {
    override fun toString(): String {
        return "TOP: $rank \n " +
                "NAME: $name \n " +
                "LINK: $link \n " +
                "REASON: $reason"
    }
}