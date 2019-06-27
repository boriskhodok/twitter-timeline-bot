package ru.khodok.twittertimeline.twitter

import com.github.scribejava.core.model.*
import com.github.scribejava.core.oauth.OAuth10aService
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

import kotlinx.serialization.parseList
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import ru.khodok.twittertimeline.telegram_bot.Settings
import java.io.File
import java.util.*
import kotlin.NoSuchElementException


class TwitterApi(sessionUUID: UUID) {
    private var sessionUUID: UUID

    private var service: OAuth10aService = OAuthService.INSTANCE.get()

    private var requestToken: String
    private var requestTokenSecret: String

    private var authorizationUrl: String


    private lateinit var apiToken: String
    private lateinit var apiTokenSecret: String

    private var isAuthorized: Boolean = false


    init {

        this.sessionUUID = sessionUUID

        val recentApiKeys = getLastApiKey(sessionUUID)

        if (recentApiKeys != null) {

            apiToken = recentApiKeys.apiToken
            apiTokenSecret = recentApiKeys.apiTokenSecret

            requestToken = recentApiKeys.requestToken
            requestTokenSecret = recentApiKeys.requestTokenSecret

            authorizationUrl = recentApiKeys.authorizationUrl

            if (apiToken != "") isAuthorized = true

        } else {
            this.requestToken = service.requestToken.token
            this.requestTokenSecret = service.requestToken.tokenSecret
            this.authorizationUrl =
                service.getAuthorizationUrl(OAuth1RequestToken(requestToken, requestTokenSecret))

            addApiKey(
                sessionUUID,
                requestToken,
                requestTokenSecret,
                authorizationUrl
            )

        }
    }

    fun getIsAuthorized(): Boolean {
        return this.isAuthorized
    }

    fun getAuthorizationUrl(): String? {
        return authorizationUrl
    }

    fun authorizeByPIN(pin: String) {
        val rt = OAuth1RequestToken(this.requestToken, this.requestTokenSecret)
        val accessToken = service.getAccessToken(rt, pin)
        this.apiToken = accessToken.token
        this.apiTokenSecret = accessToken.tokenSecret
        addApiKey(
            this.sessionUUID,
            this.requestToken,
            this.requestTokenSecret,
            this.authorizationUrl,
            this.apiToken,
            this.apiTokenSecret
        )
        isAuthorized = true
    }

    fun String.utf8(): String = java.net.URLEncoder.encode(this, "UTF-8")

    fun get(url: String, parameters: Map<String, String> = mapOf()): String? {
        val parametersString =
            parameters.map { (k, v) ->
                "${k.utf8()}=${v.utf8()}"
            }.joinToString("&")
        val request = OAuthRequest(Verb.GET, "$url?$parametersString")
        val accessToken = OAuth1AccessToken(this.apiToken, this.apiTokenSecret)
        this.service.signRequest(accessToken, request)
        val response = service.execute(request)
        return response.body
    }

    @UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
    fun getTimeline(count: String = "20", lastTweet: Long = 0): List<Tweet> {
        val parameters = mutableMapOf<String, String>()
        parameters["count"] = count
        parameters["tweet_mode"] = "extended"
        if (lastTweet > 0)
            parameters["since_id"] = lastTweet.toString()
        val response =
            this.get(
                "https://api.twitter.com/1.1/statuses/home_timeline.json",
                parameters
            )

        return tweetListDeserialize(response)
    }

    @ImplicitReflectionSerializer
    private fun tweetListDeserialize(response: String?): List<Tweet> {
        val json = Json(JsonConfiguration.Stable)
        var tweetList: List<Tweet> = listOf()
        try {
            tweetList = json.parseList(response!!)
        } catch (e: Exception) {
            val f = File("dump.json")
            val path = f.absolutePath
            f.writeText(response!!)
            println("JSON dumped to $path")
        }
        return tweetList
    }

    companion object {
        private fun addApiKey(
            sessionUUID: UUID,
            requestToken: String,
            requestTokenSecret: String,
            authorizationUrl: String,
            apiToken: String = "",
            apiTokenSecret: String = ""

        ): APIKey {
            return transaction {
                APIKey.new {
                    this.sessionUUID = sessionUUID
                    this.requestToken = requestToken
                    this.requestTokenSecret = requestTokenSecret
                    this.apiToken = apiToken
                    this.apiTokenSecret = apiTokenSecret
                    this.authorizationUrl = authorizationUrl
                    this.dateAdded = DateTime.now()
                }
            }
        }

        private fun getLastApiKey(sessionUUID: UUID): APIKey? {
            return transaction {
                val apiKeys =
                    APIKey.find {
                        APIKeys.sessionUUID eq sessionUUID
                    }.orderBy(
                        APIKeys.dateAdded to SortOrder.DESC
                    )
                try {
                    apiKeys.first()
                } catch (e: NoSuchElementException) {
                    null
                }
            }
        }
    }
}



