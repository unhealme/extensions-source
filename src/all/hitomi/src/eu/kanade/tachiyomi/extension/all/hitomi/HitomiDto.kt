package eu.kanade.tachiyomi.extension.all.hitomi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonPrimitive

interface HasTagForm {
    val tagged: String
}

@Serializable
class Gallery(
    val id: JsonPrimitive,
    val galleryurl: String,
    val title: String,
    @SerialName("japanese_title")
    val japaneseTitle: String?,
    val date: String,
    val type: String?,
    val language: String?,
    val tags: List<Tag>?,
    val artists: List<Artist>?,
    val groups: List<Group>?,
    val characters: List<Character>?,
    val parodys: List<Parody>?,
    val files: List<ImageFile>,
)

@Serializable
class ImageFile(
    val hash: String,
    private val name: String,
) {
    val isGif get() = name.endsWith(".gif") || name.endsWith(".webp")
}

@Serializable
class Tag(
    private val female: JsonPrimitive?,
    private val male: JsonPrimitive?,
    private val tag: String,
) : HasTagForm {
    override val tagged
        get() = (
            if (female?.content == "1") {
                "Female:"
            } else if (male?.content == "1") {
                "Male:"
            } else {
                "Tag:"
            }
            ) + tag.toCamelCase()
}

@Serializable
class Artist(
    private val artist: String,
) : HasTagForm {
    val formatted get() = artist.toCamelCase()
    override val tagged get() = "Artist:$formatted"
}

@Serializable
class Group(
    private val group: String,
) : HasTagForm {
    val formatted get() = group.toCamelCase()
    override val tagged get() = "Group:$formatted"
}

@Serializable
class Character(
    private val character: String,
) : HasTagForm {
    override val tagged get() = "Character:${character.toCamelCase()}"
}

@Serializable
class Parody(
    private val parody: String,
) : HasTagForm {
    override val tagged get() = "Series:${parody.toCamelCase()}"
}

fun List<HasTagForm>.tagAll(): List<String> = this.map { it.tagged }

private fun String.toCamelCase(): String {
    val result = StringBuilder(length)
    var capitalize = true
    for (char in this) {
        result.append(
            if (capitalize) {
                char.uppercase()
            } else {
                char.lowercase()
            },
        )
        capitalize = char.isWhitespace()
    }
    return result.toString()
}
