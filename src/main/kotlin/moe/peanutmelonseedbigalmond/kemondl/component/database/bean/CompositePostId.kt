package moe.peanutmelonseedbigalmond.kemondl.component.database.bean

import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
class CompositePostId : Serializable {
    lateinit var postId: String
    lateinit var creatorId:String
    lateinit var site:String

    companion object {
        private const val serialVersionUID = 263189307917957L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CompositePostId

        if (postId != other.postId) return false
        if (creatorId != other.creatorId) return false
        if (site != other.site) return false

        return true
    }

    override fun hashCode(): Int {
        var result = postId.hashCode()
        result = 31 * result + creatorId.hashCode()
        result = 31 * result + site.hashCode()
        return result
    }

}