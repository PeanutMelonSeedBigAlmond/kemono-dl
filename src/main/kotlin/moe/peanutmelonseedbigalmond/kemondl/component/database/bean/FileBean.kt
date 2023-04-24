package moe.peanutmelonseedbigalmond.kemondl.component.database.bean

import jakarta.persistence.*

@Entity
@Table(name = "file")
class FileBean {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int? = null

    @Column
    lateinit var path: String

    @Column
    lateinit var name: String

    @Column
    lateinit var type: String // cover attachment

    @ManyToOne(cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    @JoinColumns(
        value = [
            JoinColumn(name = "postId", referencedColumnName = "postId", ),
            JoinColumn(name = "creator", referencedColumnName = "creatorId", ),
            JoinColumn(name = "site", referencedColumnName = "site", )
        ]
    )
    lateinit var post: PostBean

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FileBean

        if (path != other.path) return false
        if (name != other.name) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }
}