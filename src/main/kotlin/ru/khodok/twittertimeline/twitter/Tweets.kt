package ru.khodok.twittertimeline.twitter

import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.InlineKeyboardMarkup
import org.joda.time.DateTime
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class Tweets {
    companion object {

        fun formatMessage(tweet: Tweet): Multi {
            val links = mutableListOf<String>()
            val mskZone = ZoneId.of("Europe/Moscow")
            val utcZone = ZoneId.of("UTC")
            val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss Z uuuu")
            val dateTime = LocalDateTime.parse(tweet.created_at, formatter)
            val utcTime = dateTime.atZone(utcZone)
            val moscowTime = utcTime.withZoneSameInstant(mskZone)
            val moscowTimeString = formatter.format(moscowTime)

            val media = tweet.extended_entities?.media

            media?.forEach { m ->
                links.add("${m.media_url_https}\n")
            }

            var filePaths = mutableListOf<String>()
            if (links.size > 0)
                filePaths = downloadFiles(links)
            val text = tweet.full_text

            return Multi(
                    """
                    [${tweet.user.name}](https://twitter.com/${tweet.user.screen_name}/)
                    `@${tweet.user.screen_name}`
                    `($moscowTimeString)`: 
                    $text
                    """.trimIndent(), filePaths
            )
        }

        fun downloadFiles(links: MutableList<String>): MutableList<String> {
            val filePaths = mutableListOf<String>()
            for (link in links) {
                val url = URL(link)
                val `in` = url.openStream()
                val path = Paths.get(url.path.split("/").last())
                Files.copy(`in`, path, StandardCopyOption.REPLACE_EXISTING)
                filePaths.add(path.toString())

            }

            return filePaths
        }
    }

}

class TweetInlineKeyboard (var tweet: Tweet) {

    fun generate(): InlineKeyboardMarkup {


        val buttonScreenName = InlineKeyboardButton("${tweet.user.name}\n@${tweet.user.screen_name}", url = "https://twitter.com/${tweet.user.screen_name}")
        val buttonTweetText = InlineKeyboardButton(tweet.full_text.toString(), callbackData = "twitText")
        return InlineKeyboardMarkup(
            listOf(
                listOf(buttonScreenName),
                listOf(buttonTweetText)
            )
        )
    }

}

data class Multi(val text: String, val files: List<String>)
