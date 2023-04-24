package moe.peanutmelonseedbigalmond.kemondl.component.database.bean

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.Table
import java.io.Serializable


@Entity
@Table(name = "creator")
class CreatorBean:Serializable {
    @Id
    lateinit var id:String

    @Column
    lateinit var username:String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CreatorBean

        if (username != other.username) return false

        return true
    }

    override fun hashCode(): Int {
        return username.hashCode()
    }

    companion object{
        private const val serialVersionUID=275955964523483L
    }
}