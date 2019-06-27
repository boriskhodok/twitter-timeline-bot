package ru.khodok.twittertimeline.telegram

import kotlinx.serialization.ImplicitReflectionSerializer
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import ru.khodok.twittertimeline.twitter.Tweet
import ru.khodok.twittertimeline.twitter.TwitterApi
import java.util.*

class Session(val telegramId: Long) {
    fun getAuthorizationUrl(): String? {
        numberApiRequests += 1
        return this.api.getAuthorizationUrl()
    }

    fun getTimeLine(lastTweet: Long = 0L): List<Tweet> {
        numberApiRequests += 1
        return this.api.getTimeline(lastTweet = lastTweet)
    }

    fun getIsAuthorized(): Boolean {
        numberApiRequests += 1
        return this.api.getIsAuthorized()
    }

    fun authorizeByPIN(pin: String) {
        numberApiRequests += 1
        return this.api.authorizeByPIN(pin)
    }

    fun updateLastTweet(tweetId: Long) {
        this.lastTweetId = tweetId
        transaction {
            Sessions.update({ Sessions.sessionUUID eq sessionUUID }) {
                it[lastTweetId] = tweetId
            }
        }
    }

    private fun getSession(telegramId: Long): ResultRow {
        return transaction {
            val sessionsQuery =
                Sessions.select {
                    Sessions.telegramId eq telegramId
                }.orderBy(
                    Sessions.loginTime to SortOrder.DESC
                )
            if (sessionsQuery.count() > 0) {
                return@transaction sessionsQuery.first()
            } else {
                new(telegramId)
                return@transaction getSession(telegramId)
            }
        }
    }

    fun new(telegramId: Long) {
        Sessions.insert {
            it[Sessions.telegramId] = telegramId
            it[sessionUUID] = UUID.randomUUID()
            it[numberApiRequests] = 0
            it[lastTweetId] = 0
            it[loginTime] = DateTime.now()
        }
    }

    fun getNumberApiRequests(): Long{
        return this.numberApiRequests
    }


    private var sessionUUID: UUID
    private var loginTime: DateTime
    private var numberApiRequests: Long = 0
    var lastTweetId: Long = 0
    private var api: TwitterApi


    init {
        val sessionResult = getSession(telegramId)
        sessionResult.let { resultRow ->
            this.sessionUUID = resultRow[Sessions.sessionUUID]
            this.loginTime = resultRow[Sessions.loginTime]
            this.numberApiRequests = resultRow[Sessions.numberApiRequests]
            this.lastTweetId = resultRow[Sessions.lastTweetId]
        }
        this.api = TwitterApi(sessionUUID)
    }

    companion object Manager {
        private val sessions = mutableMapOf<Long, Session>()

        private fun newSession(telegramId: Long): Session {
            val session = Session(telegramId)
            sessions[telegramId] = session
            return session
        }

        fun getSession(telegramId: Long): Session {
            if (sessions.keys.contains(telegramId)) {
                sessions[telegramId]
                return sessions[telegramId]!!
            }
            else return newSession(telegramId)
        }
    }

}