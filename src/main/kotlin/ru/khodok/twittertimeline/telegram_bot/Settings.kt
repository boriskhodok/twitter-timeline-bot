package ru.khodok.twittertimeline.telegram_bot

class Settings {

    companion object {
        val twitterOauthConsumerKey: String = System.getenv()["TWITTER_OAUTH_CONSUMER_KEY"].toString()
        val twitterOauthConsumerKeySecret: String = System.getenv()["TWITTER_OAUTH_CONSUMER_KEY_SECRET"].toString()
        val dbConnectionString: String = System.getenv()["TG_BOT_DB_CONNECTION_STRING"].toString()
        val dbDriver: String = System.getenv()["TG_BOT_DB_DRIVER"].toString()
        val dbUser: String = System.getenv()["TG_BOT_DB_USER"].toString()
        val dbPassword: String = System.getenv()["TG_BOT_DB_PASSWORD"].toString()
        val botToken: String = System.getenv()["TG_BOT_TOKEN"].toString()
        val debugMode: String = System.getenv()["TG_BOT_DEBUG_MODE"].toString()
    }
}



