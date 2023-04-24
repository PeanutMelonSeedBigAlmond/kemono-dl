package moe.peanutmelonseedbigalmond.kemondl.component.database

import moe.peanutmelonseedbigalmond.kemondl.component.database.bean.CompositePostId
import moe.peanutmelonseedbigalmond.kemondl.component.database.bean.CreatorBean
import moe.peanutmelonseedbigalmond.kemondl.component.database.bean.PostBean
import org.hibernate.Session
import org.hibernate.cfg.Configuration
import java.io.Closeable


object Database : Closeable {
    @JvmStatic
    private val configuration = Configuration().configure()

    @JvmStatic
    private val sessionFactory = configuration.buildSessionFactory()

    private val session: Session
        get() = sessionFactory.openSession()

    fun addCreator(creatorBean: CreatorBean) {
        session.use {
            val trans = it.beginTransaction()
            try {
                it.merge(creatorBean)
                trans.commit()
            } catch (e: Exception) {
                e.printStackTrace()
                trans.rollback()
                throw e
            }
        }
    }

    fun addPostAndCreator(postBean: PostBean, creatorBean: CreatorBean) {
        session.use {
            val trans = it.beginTransaction()
            try {
                it.merge(creatorBean)
                it.merge(postBean)
                trans.commit()
            } catch (e: Exception) {
                e.printStackTrace()
                trans.rollback()
                throw e
            }
        }
    }

    fun addPost(postBean: PostBean) {
        session.use {
            val trans = it.beginTransaction()
            try {
                it.merge(postBean)
                trans.commit()
            } catch (e: Exception) {
                e.printStackTrace()
                trans.rollback()
                throw e
            }
        }
    }

    fun findPostByCompositePostId(postId: CompositePostId):PostBean?{
        session.use {
            return session.get(PostBean::class.java,postId)
        }
    }

    override fun close() {
        sessionFactory.close()
    }
}