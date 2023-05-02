package moe.peanutmelonseedbigalmond.kemondl.component.database.bean

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "post")
class PostBean {
    @EmbeddedId
    @AttributeOverrides(value = [
        AttributeOverride(name = "postId", column = Column(name = "postId")),
    AttributeOverride(name = "site", column = Column(name = "site")),
    AttributeOverride(name = "creatorId", column = Column(name = "creatorId"))
    ])
    lateinit var key: CompositePostId

    @Column(columnDefinition = "CLOB")
    lateinit var content: String

    @Column
    lateinit var published: Date

    @Column
    lateinit var title: String

    @OneToMany(
        mappedBy = "post",
        cascade = [CascadeType.ALL]
    )
    lateinit var files: List<FileBean>

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostBean

        if (key != other.key) return false
        if (content != other.content) return false
        if (published != other.published) return false
        if (title != other.title) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + content.hashCode()
        result = 31 * result + published.hashCode()
        result = 31 * result + title.hashCode()
        return result
    }
}