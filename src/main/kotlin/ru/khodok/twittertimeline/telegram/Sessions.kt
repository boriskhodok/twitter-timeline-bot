package ru.khodok.twittertimeline.telegram

import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime
import java.util.*

object Sessions: Table() {
    val sessionId: Column<Int> = integer("id").autoIncrement().primaryKey()
    val sessionUUID: Column<UUID> = uuid("uuid").uniqueIndex()
    val telegramId: Column<Long> = long("telegram_id")
    val loginTime: Column<DateTime> = datetime("login_time")
    val numberApiRequests: Column<Long> = long("number_api_requests")
    val lastTweetId: Column<Long> = long("last_tweet_id")
}