package ru.khodok.twittertimeline.twitter

import com.github.scribejava.apis.TwitterApi
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.oauth.OAuth10aService
import ru.khodok.twittertimeline.telegram_bot.Settings

class OAuthService private constructor() {

    private var service: OAuth10aService? = ServiceBuilder(Settings.twitterOauthConsumerKey)
        .apiSecret(Settings.twitterOauthConsumerKeySecret)
        .callback("oob")
        .build(TwitterApi.instance())

    init {
        INSTANCE = this
    }

    fun get() : OAuth10aService {
        return this.service!!
    }

    companion object {
        lateinit var INSTANCE: OAuthService

        init {
            OAuthService()
        }
    }
}