package ru.khodok.twittertimeline.twitter

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.joda.time.DateTime
import java.util.*

object APIKeys : IntIdTable() {
    var sessionUUID: Column<UUID> = uuid("session_uuid")
    var apiToken: Column<String> = varchar("api_token", 250)
    var apiTokenSecret: Column<String> = varchar("api_token_secret", 250)
    var requestToken: Column<String> = varchar("request_token", 250)
    var requestTokenSecret: Column<String> = varchar("request_token_secret", 250)
    var authorizationUrl: Column<String> = varchar("authorization_url", 250)
    var dateAdded: Column<DateTime> = datetime("date_added")
}

class APIKey(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<APIKey>(APIKeys)

    var sessionUUID by APIKeys.sessionUUID
    var apiToken by APIKeys.apiToken
    var apiTokenSecret by APIKeys.apiTokenSecret
    var requestToken by APIKeys.requestToken
    var requestTokenSecret by APIKeys.requestTokenSecret
    var authorizationUrl by  APIKeys.authorizationUrl
    var dateAdded by APIKeys.dateAdded
}