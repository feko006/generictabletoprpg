package com.feko.generictabletoprpg.common

import androidx.room.Insert
import androidx.room.Update

abstract class BaseDao<TEntity, TCore> :
    InsertAllPort<TCore>,
    GetAllPort<TCore>,
    GetByIdPort<TCore>
        where TCore : Named,
              TEntity : Named,
              TEntity : MutableIdentifiable,
              TEntity : CoreConvertible<TCore> {
    lateinit var logger: Logger

    @Insert
    abstract fun insert(entity: TEntity): Long

    @Update
    abstract fun update(entity: TEntity)

    final override fun insertAll(list: List<@JvmSuppressWildcards TCore>): Result<Boolean> {
        val errors = mutableListOf<Exception>()
        list.forEach { item ->
            try {
                val entity = getEntityFromCoreModel(item)
                insertOrUpdate(entity)
            } catch (e: Exception) {
                logger.error(e, "Saving spell with name ${item.name} failed.")
                errors.add(e)
            }
        }
        return Result.success(errors.isEmpty())
    }

    private fun insertOrUpdate(entity: TEntity) {
        val existingEntityId = getEntityId(entity)
        return if (existingEntityId == null) {
            val newId = insert(entity)
            setEntityId(entity, newId)
        } else {
            setEntityId(entity, existingEntityId)
            update(entity)
        }
    }

    protected abstract fun getEntityFromCoreModel(item: TCore): TEntity

    private fun getEntityId(entity: TEntity): Long? = getEntityIdByName(entity.name)

    protected open fun getEntityIdByName(name: String): Long? = throw NotImplementedError()

    private fun setEntityId(entity: TEntity, existingEntityId: Long) {
        entity.id = existingEntityId
    }

    final override fun getAllSortedByName(): List<TCore> =
        getAllSortedByNameInternal()
            .map { it.toCoreModel() }


    protected open fun getAllSortedByNameInternal(): List<TEntity> = throw NotImplementedError()

    final override fun getById(id: Long): TCore =
        getByIdInternal(id).toCoreModel()

    protected open fun getByIdInternal(id: Long): TEntity = throw NotImplementedError()
}