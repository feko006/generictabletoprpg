package com.feko.generictabletoprpg.common

import androidx.room.Insert
import androidx.room.Update

abstract class BaseDao<T> {
    @Insert
    abstract fun insert(entity: T): Long

    @Update
    abstract fun update(entity: T)

    /** Needs to be overridden only if [insertOrUpdate] method is used. */
    open fun getEntityId(entity: T): Long? {
        throw NotImplementedError()
    }

    /** Needs to be overridden only if [insertOrUpdate] method is used. */
    open fun setEntityId(entity: T, existingEntityId: Long) {
        throw NotImplementedError()
    }

    /** Implement [getEntityId] and [setEntityId] to use. */
    fun insertOrUpdate(entity: T) {
        val existingEntityId = getEntityId(entity)
        return if (existingEntityId == null) {
            val newId = insert(entity)
            setEntityId(entity, newId)
        } else {
            setEntityId(entity, existingEntityId)
            update(entity)
        }
    }
}