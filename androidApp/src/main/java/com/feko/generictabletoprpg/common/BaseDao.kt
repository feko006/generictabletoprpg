package com.feko.generictabletoprpg.common

import androidx.room.Insert
import androidx.room.Update

abstract class BaseDao<TEntity, TCore> :
    IInsertAll<TCore>,
    IInsertOrUpdate<TCore>,
    IGetAll<TCore>,
    IGetAllByParent<TCore>,
    IGetById<TCore>
        where TCore : Named,
              TEntity : Named,
              TEntity : MutableIdentifiable,
              TEntity : CoreConvertible<TCore> {
    lateinit var logger: ILogger

    @Insert
    abstract fun insert(entity: TEntity): Long

    @Update
    abstract fun update(entity: TEntity)

    override fun insertAll(list: List<@JvmSuppressWildcards TCore>): Result<Boolean> {
        val errors = mutableListOf<Exception>()
        list.forEach { item ->
            try {
                insertOrUpdate(item)
            } catch (e: Exception) {
                logger.error(e, "Saving spell with name ${item.name} failed.")
                errors.add(e)
            }
        }
        return Result.success(errors.isEmpty())
    }

    override fun insertOrUpdate(item: TCore): Long {
        val entity = getEntityFromCoreModel(item)
        val existingEntityId = getEntityId(entity)
        return if (existingEntityId == null) {
            val newId = insert(entity)
            setEntityId(entity, newId)
            newId
        } else {
            setEntityId(entity, existingEntityId)
            update(entity)
            existingEntityId
        }
    }

    protected abstract fun getEntityFromCoreModel(item: TCore): TEntity

    protected open fun getEntityId(entity: TEntity): Long? = getEntityIdByName(entity.name)

    protected open fun getEntityIdByName(name: String): Long? = throw NotImplementedError()

    private fun setEntityId(entity: TEntity, existingEntityId: Long) {
        entity.id = existingEntityId
    }

    final override fun getAllSortedByName(): List<TCore> =
        getAllSortedByNameInternal()
            .map { it.toCoreModel() }

    final override fun getAllSortedByName(parentId: Long): List<TCore> =
        getAllSortedByNameInternal(parentId)
            .map { it.toCoreModel() }

    protected open fun getAllSortedByNameInternal(): List<TEntity> = throw NotImplementedError()
    protected open fun getAllSortedByNameInternal(parentId: Long): List<TEntity> =
        throw NotImplementedError()

    final override fun getById(id: Long): TCore =
        getByIdInternal(id).toCoreModel()

    protected open fun getByIdInternal(id: Long): TEntity = throw NotImplementedError()
}