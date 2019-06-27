package ru.khodok.twittertimeline.tests

import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.parseList
import org.jetbrains.exposed.sql.Database
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import ru.khodok.twittertimeline.telegram.Session
import ru.khodok.twittertimeline.twitter.TwitterApi
import ru.khodok.twittertimeline.twitter.Tweet
import java.io.File
import java.util.*

@ImplicitReflectionSerializer
class DeserializerTest {
    companion object {
        @BeforeAll
        fun before() {
            Database.connect(
                "jdbc:postgresql://localhost:5432/bot",
                driver = "org.postgresql.Driver",
                user = "postgres",
                password = ""
            )
        }
    }

    @Test
    fun testDeserializationFromDump() {
        val jsonText = DeserializerTest::class.java.getResource("/dump.json").readText()
        val json = Json(JsonConfiguration.Stable)
        val tweetList = json.parseList<Tweet>(jsonText)
        tweetList.forEach { tweet ->
            print(tweet.id)
        }
        assert(true)

    }



}