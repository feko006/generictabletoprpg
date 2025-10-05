package com.feko.generictabletoprpg.shared.common.data.local

import androidx.room.Insert
import androidx.room.Update
import com.feko.generictabletoprpg.shared.common.domain.model.ICoreConvertible
import com.feko.generictabletoprpg.shared.common.domain.model.IMutableIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.shared.logger

abstract class BaseDao<TEntity, TCore> :
    IInsertAllDao<TCore>,
    IInsertOrUpdateDao<TCore>,
    IGetAllDao<TCore>,
    IGetAllByParentDao<TCore>,
    IGetByIdDao<TCore>
        where TCore : INamed,
              TEntity : INamed,
              TEntity : IMutableIdentifiable,
              TEntity : ICoreConvertible<TCore> {

    @Insert
    abstract suspend fun insert(entity: TEntity): Long

    @Update
    abstract suspend fun update(entity: TEntity)

    override suspend fun insertAll(list: List<TCore>): Result<Boolean> {
        val errors = mutableListOf<Exception>()
        list.forEach { item ->
            try {
                insertOrUpdate(item)
            } catch (e: Exception) {
                logger.error(e) { "Saving entity with name ${item.name} failed." }
                errors.add(e)
            }
        }
        return Result.success(errors.isEmpty())
    }

    override suspend fun insertOrUpdate(item: TCore): Long {
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

    protected open suspend fun getEntityId(entity: TEntity): Long? = getEntityIdByName(entity.name)

    protected open suspend fun getEntityIdByName(name: String): Long? = throw NotImplementedError()

    private fun setEntityId(entity: TEntity, existingEntityId: Long) {
        entity.id = existingEntityId
    }

    final override suspend fun getAllSortedByName(): List<TCore> =
        getAllSortedByNameInternal()
            .map { it.toCoreModel() }

    final override suspend fun getAllSortedByName(parentId: Long): List<TCore> =
        getAllSortedByNameInternal(parentId)
            .map { it.toCoreModel() }

    protected open suspend fun getAllSortedByNameInternal(): List<TEntity> =
        throw NotImplementedError()

    protected open suspend fun getAllSortedByNameInternal(parentId: Long): List<TEntity> =
        throw NotImplementedError()

    final override suspend fun getById(id: Long): TCore =
        getByIdInternal(id).toCoreModel()

    protected open suspend fun getByIdInternal(id: Long): TEntity = throw NotImplementedError()
}