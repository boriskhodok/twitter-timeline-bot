package ru.khodok.twittertimeline.twitter

import kotlinx.serialization.*
import kotlinx.serialization.internal.*

@Serializable
data class Tweet(
    val created_at: String,
    val id: Long,
    val id_str: String,
    val text: String? = null,
    val full_text: String? = null,
    val truncated: Boolean,
    val entities: Entity,
    val extended_entities: ExtendedEntity? = null,
    val source: String,
    val display_text_range: List<Int>? = null,
    val quoted_status_id: Long? = null,
    val quoted_status_id_str: String? = null,
    val quoted_status: RetweetedStatus? = null,
    val quoted_status_permalink: QuotedStatusPermalink? = null,
    val in_reply_to_status_id: Long?,
    val in_reply_to_status_id_str: String?,
    val in_reply_to_user_id: Long?,
    val in_reply_to_user_id_str: String?,
    val in_reply_to_screen_name: String?,
    val user: User,
    val geo: Geo? = null,
    val coordinates: Geo? = null,
    val place: Place?,
    val contributors: String?,
    val is_quote_status: Boolean,
    val retweeted_status: RetweetedStatus? = null,
    val retweet_count: Long,
    val favorite_count: Long,
    val favorited: Boolean,
    val retweeted: Boolean,
    val possibly_sensitive: Boolean? = null,
    val possibly_sensitive_appealable: Boolean? = null,
    val lang: String
)

@Serializable
data class QuotedStatusPermalink(
    val display: String,
    val expanded: String,
    val url: String
)

@Serializable
data class Place(
    val id: String,
    val url: String,
    val place_type: String,
    val name: String,
    val full_name: String,
    val country_code: String,
    val country: String,
    val contained_within: List<String>,
    val bounding_box: BoundingBox,
    val attributes: PlaceAttributes? = null
)

@Serializable
data class PlaceAttributes(
    val id: Long? = null
)


@Serializable
data class BoundingBox(
    val coordinates: List<List<List<Double>>>,
    val type: String
)

@Serializable
data class Media(
    val display_url: String,
    val expanded_url: String? = null,
    val id: Long,
    val id_str: String,
    val indices: List<Int>,
    val media_url: String,
    val media_url_https: String,
    val sizes: Sizes,
    val source_status_id: Long? = null,
    val source_status_id_str: String? = null,
    val source_user_id: Long? = null,
    val source_user_id_str: String? = null,
    val type: String,
    val url: String
)

@Serializable
data class Geo(
    val type: String,
    val coordinates: List<Double>
)

@Serializable
data class Sizes(
    val large: Large,
    val medium: Medium,
    val small: Small,
    val thumb: Thumb
)

@Serializable
data class Medium(
    val h: Int,
    val resize: String,
    val w: Int
)

@Serializable
data class Large(
    val h: Int,
    val resize: String,
    val w: Int
)

@Serializable
data class Small(
    val h: Int,
    val resize: String,
    val w: Int
)

@Serializable
data class Thumb(
    val h: Int,
    val resize: String,
    val w: Int
)


@Serializable
data class Entity(
    val hashtags: List<Hashtag>? = listOf(),
    val symbols: List<String?> = listOf(),
    val user_mentions: List<UserMention?> = listOf(),
    val urls: List<Url?>? = null,
    val media: List<Media?>? = null
)

@Serializable
data class Hashtag(
    val text: String,
    val indices: List<Int>
)

@Serializable
data class RetweetedStatus(
    val created_at: String,
    val id: Long,
    val id_str: String,
    val text: String? = null,
    val full_text: String? = null,
    val display_text_range: List<Int>? = null,
    val truncated: Boolean,
    val entities: Entity,
    val extended_entities: ExtendedEntity? = null,
    val source: String,
    val in_reply_to_status_id: Long?,
    val in_reply_to_status_id_str: String?,
    val in_reply_to_user_id: Long?,
    val in_reply_to_user_id_str: String?,
    val in_reply_to_screen_name: String?,
    val user: User,
    val `protected`: Boolean? = null,
    val contributors_enabled: Boolean? = null,
    val default_profile: Boolean? = null,
    val default_profile_image: Boolean? = null,
    val favourites_count: Int? = null,
    val follow_request_sent: Boolean? = null,
    val followers_count: Int? = null,
    val following: Boolean? = null,
    val friends_count: Int? = null,
    val geo_enabled: Boolean? = null,
    val has_extended_profile: Boolean? = null,
    val is_translation_enabled: Boolean? = null,
    val is_translator: Boolean? = null,
    val lang: String? = null,
    val listed_count: Int? = null,
    val notifications: Boolean? = null,
    val geo: Geo? = null,
    val coordinates: String?,
    val place: Place?,
    val contributors: String? = null,
    val is_quote_status: Boolean,
    val retweet_count: Long,
    val favorite_count: Long,
    val favorited: Boolean,
    val retweeted: Boolean,
    val possibly_sensitive: Boolean? = null,
    val possibly_sensitive_appealable: Boolean? = null,
    val quoted_status_id: Long? = null,
    val quoted_status_id_str: String? = null,
    val quoted_status: RetweetedStatus? = null,
    val quoted_status_permalink: QuotedStatusPermalink? = null
)


@Serializable
data class ExtendedEntity(
    val media: List<ExtendedMedia>
)

@Serializable
data class ExtendedMedia(
    val additional_media_info: AdditionalMediaInfo? = null,
    val display_url: String,
    val expanded_url: String,
    val id: Long,
    val id_str: String,
    val indices: List<Int>,
    val media_url: String,
    val media_url_https: String,
    val sizes: Sizes,
    val source_status_id: Long? = null,
    val source_status_id_str: String? = null,
    val source_user_id: Long? = null,
    val source_user_id_str: String? = null,
    val type: String,
    val url: String,
    val video_info: VideoInfo? = null
)

@Serializable
data class SourceUser(
    val `protected`: Boolean,
    val contributors_enabled: Boolean,
    val created_at: String,
    val default_profile: Boolean,
    val default_profile_image: Boolean,
    val description: String,
    val entities: UserEntitity,
    val favourites_count: Int,
    val follow_request_sent: Boolean,
    val followers_count: Int,
    val following: Boolean,
    val friends_count: Int,
    val geo_enabled: Boolean,
    val has_extended_profile: Boolean,
    val id: Long,
    val id_str: String,
    val is_translation_enabled: Boolean,
    val is_translator: Boolean,
    val lang: String?,
    val listed_count: Int,
    val location: String,
    val name: String,
    val notifications: Boolean,
    val profile_background_color: String,
    val profile_background_image_url: String,
    val profile_background_image_url_https: String,
    val profile_background_tile: Boolean,
    val profile_banner_url: String? = null,
    val profile_image_url: String? = null,
    val profile_image_url_https: String? = null,
    val profile_link_color: String? = null,
    val profile_sidebar_border_color: String? = null,
    val profile_sidebar_fill_color: String? = null,
    val profile_text_color: String? = null,
    val profile_use_background_image: Boolean? = null,
    val screen_name: String,
    val statuses_count: Int,
    val time_zone: String?,
    val translator_type: String,
    val url: String?,
    val utc_offset: String?,
    val verified: Boolean
)


@Serializable
data class Url(
    val display_url: String? = null,
    val expanded_url: String? = null,
    val indices: List<Int>,
    val url: String
)

@Serializable
data class Urls(
    val urls: List<Url>? = listOf()
)

@Serializable
data class UserEntitity(
    val url: Urls? = null,
    val description: Urls
)

@Serializable
data class AdditionalMediaInfo(
    val title: String? = null,
    val description: String? = null,
    val monetizable: Boolean,
    val embeddable: Boolean? = null,
    val source_user: SourceUser? = null
)

@Serializable
data class Entities(
    val description: Description
)

@Serializable
data class Description(
    val urls: List<String>? = null
)

@Serializable
data class VideoInfo(
    val aspect_ratio: List<Int>,
    val duration_millis: Int? = null,
    val variants: List<Variant> = listOf()
)

@Serializable
data class Variant(
    val bitrate: Int = 0,
    val content_type: String,
    val url: String
)

@Serializable
data class UserMention(
    val id: Long,
    val id_str: String,
    val indices: List<Int>,
    val name: String,
    val screen_name: String
)

@Serializable
data class User(
    val id: Long,
    val id_str: String,
    val name: String,
    val screen_name: String,
    val location: String,
    val description: String,
    val url: String?,
    val entities: UserEntitity,
    val protected: Boolean,
    val followers_count: Long,
    val friends_count: Long,
    val listed_count: Long,
    val created_at: String,
    val favourites_count: Long,
    val utc_offset: String?,
    val time_zone: String?,
    val geo_enabled: Boolean,
    val verified: Boolean,
    val statuses_count: Long,
    val lang: String?,
    val contributors_enabled: Boolean,
    val is_translator: Boolean,
    val is_translation_enabled: Boolean,
    val profile_background_color: String,
    val profile_background_image_url: String? = null,
    val profile_background_image_url_https: String? = null,
    val profile_background_tile: Boolean,
    val profile_image_url: String,
    val profile_image_url_https: String,
    val profile_banner_url: String? = null,
    val profile_link_color: String? = null,
    val profile_sidebar_border_color: String? = null,
    val profile_sidebar_fill_color: String? = null,
    val profile_text_color: String? = null,
    val profile_use_background_image: Boolean? = null,
    val has_extended_profile: Boolean? = null,
    val default_profile: Boolean,
    val default_profile_image: Boolean,
    val following: Boolean,
    val follow_request_sent: Boolean,
    val notifications: Boolean,
    val translator_type: String
)