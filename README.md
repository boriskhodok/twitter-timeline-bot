# Twitter Timeline Telegram bot
Simple [Telegram](https://telegram.org/) bot which could load tweets from your Twitter timeline

## Preconditions
To use this bot on your own you will need **Twitter API keys** and **Telegram Bot token**

### Twitter API Keys
You need to obtain [Twitter Developer API](https://developer.twitter.com/en/account/get-started) OAuth keys

### Telegram bot token
Go to [@BotFather](https://t.me/BotFather) Telegram bot and follow the instructions

## Environmental variables
To be able to run bot on your machine you should setup environmental variables:

Bot is using [Kotlin Exposed](https://github.com/JetBrains/Exposed) for database interaction

### TG\_BOT\_DB\_DRIVER
You can use any JDBC driver supported by Kotlin Exposed

##### Example:
`export TG_BOT_DB_DRIVER=org.postgresql.Driver`

### TG\_BOT\_DB\_CONNECTION\_STRING
Standard connection JDBC connection string for your favourite database

##### Example:
`export TG_BOT_DB_CONNECTION_STRING=jdbc:postgresql://localhost:5432/bot `

### TG\_BOT\_DB\_USER
Username to connect to database

##### Example:
`export TG_BOT_DB_USER=postgres`

### TG\_BOT\_DB\_PASSWORD
Password to connect to database

##### Example:
`export TG_BOT_DB_PASSWORD=`

### TG\_BOT\_DEBUG\_MODE
Debug mode for bot (for example is used to show *"Reset last Tweet ID"* button)

##### Example:
`export TG_BOT_DEBUG_MODE=false`


### TG\_BOT\_TOKEN
Token you obtained from **@BotFather**

### TWITTER\_OAUTH\_CONSUMER\_KEY
OAuth Consumer Key you received from **Twitter Developer API**

### TWITTER\_OAUTH\_CONSUMER\_KEY\_SECRET
OAuth Consumer Key you received from **Twitter Developer API**

## Building
`./gradlew build`

## Testing
`./gradlew test`

_No real tests developed yet_

## Running
`./gradlew run`

Go to your bot in Telegram app and check if it responds to `/start` command.
