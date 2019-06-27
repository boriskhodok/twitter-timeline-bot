package ru.khodok.twittertimeline.telegram_bot

import kotlinx.serialization.ImplicitReflectionSerializer
import me.ivmg.telegram.bot
import me.ivmg.telegram.dispatch
import me.ivmg.telegram.dispatcher.callbackQuery
import me.ivmg.telegram.dispatcher.command
import me.ivmg.telegram.dispatcher.text
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.khodok.twittertimeline.telegram.Commands
import ru.khodok.twittertimeline.telegram.Sessions
import ru.khodok.twittertimeline.twitter.APIKeys


fun main(args: Array<String>) {

    Database.connect(
        url = Settings.dbConnectionString,
        driver = Settings.dbDriver,
        user = Settings.dbUser,
        password = Settings.dbPassword
    )
    createTablesIfDontExist()


    val bot = bot {

        token = Settings.botToken

        dispatch {
            command("start") { bot, update ->
                Commands.start(bot, update)
            }

            command("resetLastTweet") { bot, update ->
                Commands.resetLastTweet(bot, update)
            }

            callbackQuery("activateUser") { bot, update ->
                Commands.activateUser(bot, update)
            }

            callbackQuery("resetLastTweet") { bot, update ->
                Commands.resetLastTweet(bot, update)
            }
            @UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
           callbackQuery("getTweets") { bot, update ->
               Commands.getTweets(bot, update)
           }

            text { bot, update ->
                Commands.text(bot, update)
            }


        }
    }
    bot.startPolling()
}



fun createTablesIfDontExist() {
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Sessions, APIKeys)
    }
}




