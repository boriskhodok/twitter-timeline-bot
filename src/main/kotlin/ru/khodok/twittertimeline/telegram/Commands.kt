package ru.khodok.twittertimeline.telegram

import kotlinx.serialization.ImplicitReflectionSerializer
import me.ivmg.telegram.Bot
import me.ivmg.telegram.entities.*
import me.ivmg.telegram.network.fold
import ru.khodok.twittertimeline.telegram_bot.Settings
import ru.khodok.twittertimeline.twitter.Tweet
import ru.khodok.twittertimeline.twitter.Tweets
import java.io.File


class Commands {
    companion object {
        fun start(bot: Bot, update: Update) {
            val telegramId = update.message!!.from!!.id
            val session = Session.getSession(telegramId)
            if (session.getIsAuthorized()) {
                showGetTweetsMessage(bot, telegramId)
            } else {
                val inlineKeyboardMarkup = InlineKeyboards.login
                bot.sendMessage(
                    chatId = update.message!!.chat.id,
                    text = """🐦Press "Login" to continue""",
                    replyMarkup = inlineKeyboardMarkup

                )
            }
        }

        private fun showGetTweetsMessage(
            bot: Bot,
            telegramId: Long
        ) {
            bot.sendMessage(
                chatId = telegramId,
                text = """🐦Twitter timeline bot""",
                replyMarkup = InlineKeyboards.getTweetsKeyboard(),
                disableNotification = true
            )
        }

        fun resetLastTweet(bot: Bot, update: Update) {
            val telegramId = update.callbackQuery!!.from.id
            val session = Session.getSession(telegramId)
            session.updateLastTweet(0)
            bot.sendMessage(
                chatId = update.callbackQuery!!.message!!.chat.id,
                text = """👍""",
                replyMarkup = InlineKeyboards.getTweetsKeyboard()
            )
        }

        fun activateUser(bot: Bot, update: Update) {
            val telegramId = update.callbackQuery!!.from.id
            val session = Session.getSession(telegramId)

            bot.sendMessage(
                chatId = telegramId,
                text = """🐦Press button to get to authorization page""",
                replyMarkup = InlineKeyboards.authKeyboard(session),
                replyToMessageId = update.callbackQuery!!.message!!.messageId
            )
        }


        fun getTweets(bot: Bot, update: Update) {
            val telegramId = update.callbackQuery!!.from.id

            val session = Session.getSession(telegramId)
            val lastTweet = session.lastTweetId
            val timelineTweets: List<Tweet>
            timelineTweets = if (lastTweet > 0) {
                session.getTimeLine(lastTweet = lastTweet)
            } else {
                session.getTimeLine()
            }
            if (timelineTweets.isNotEmpty()) {
                session.updateLastTweet(timelineTweets.first().id)

                timelineTweets.reversed().forEach { tweet ->
                    val message = Tweets.formatMessage(tweet)
                    val messageText = message.text
                    val filePaths = message.files
                    if (filePaths.isNullOrEmpty()) {
                        bot.sendMessage(
                            chatId = telegramId,
                            text = messageText,
                            parseMode = ParseMode.MARKDOWN,
                            disableNotification = true,
                            disableWebPagePreview = true
                        )
                    } else {
                        val filePath = File(filePaths[0])
                        bot.sendMessage(
                            chatId = telegramId,
                            text = messageText,
                            parseMode = ParseMode.MARKDOWN,
                            disableNotification = true,
                            disableWebPagePreview = true
                        ).fold(response = {
                            val replyToMessageId = it?.result?.messageId
                            bot.sendPhoto(
                                chatId = telegramId,
                                photo = filePath,
                                disableNotification = true,
                                replyToMessageId = replyToMessageId

                            )
                        }) { error -> error.errorBody }

                    }
                }
                showGetTweetsMessage(bot, telegramId)
            }

        }

        fun text(bot: Bot, update: Update) {
            val validationIdRegex = """(\d\d\d\d\d\d\d)""".toRegex()
            val messageText = update.message!!.text.toString()
            val telegramId = update.message!!.from!!.id
            val session = Session.getSession(telegramId)
            if (session.getIsAuthorized()) {
                showGetTweetsMessage(bot, telegramId)
            } else {
                if (validationIdRegex.containsMatchIn(messageText)) {
                    session.authorizeByPIN(messageText)
                    if (session.getIsAuthorized()) {
                        showGetTweetsMessage(bot, telegramId)
                    }
                }
            }
        }
    }
}


class InlineKeyboards {
    companion object {
        val login: InlineKeyboardMarkup = InlineKeyboardMarkup(
            listOf(
                listOf(InlineKeyboardButton("🐦 Login to Twitter", callbackData = "activateUser"))
            )
        )
        private val getTweetsButtons =
            InlineKeyboardButton(
                "Get latest tweets",
                callbackData = "getTweets"
            )

        private val resetLastTweetButton: InlineKeyboardButton =
            InlineKeyboardButton(
                "Reset last tweet (debug mode)",
                callbackData = "resetLastTweet"
            )

        fun getTweetsKeyboard(): InlineKeyboardMarkup {
                if (Settings.debugMode.toBoolean()) {
                    return InlineKeyboardMarkup(
                        listOf(
                            listOf(
                                getTweetsButtons,
                                resetLastTweetButton)
                        )
                    )
                } else {
                    return InlineKeyboardMarkup(
                        listOf(
                            listOf(
                                getTweetsButtons
                            )
                        )
                    )
                }
        }

        fun authKeyboard(session: Session): InlineKeyboardMarkup =
            InlineKeyboardMarkup(
                listOf(
                    listOf(
                        InlineKeyboardButton(
                            "Proceed to login page",
                            callbackData = "loginPageBack",
                            url = session.getAuthorizationUrl()
                        )
                    )
                )
            )
    }
}