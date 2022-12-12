package com.feko.generictabletoprpg.feat

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.Logger

@Dao
abstract class FeatDao
    : BaseDao<FeatEntity>(),
    SaveFeatsPort,
    GetAllFeatsPort,
    GetFeatByIdPort {

    lateinit var logger: Logger

    override fun save(feats: List<Feat>): Result<Boolean> {
        val errors = mutableListOf<Exception>()
        feats.forEach { feat ->
            try {
                val featEntity = FeatEntity.fromCoreModel(feat)
                insertOrUpdate(featEntity)
            } catch (e: Exception) {
                logger.error(e, "Saving spell with name ${feat.name} failed.")
                errors.add(e)
            }
        }
        return Result.success(errors.isEmpty())
    }

    @Query("select id from feats where name = :name")
    abstract fun getEntityIdInternal(name: String): Long?

    override fun getEntityId(entity: FeatEntity): Long? =
        getEntityIdInternal(entity.name)

    override fun setEntityId(entity: FeatEntity, existingEntityId: Long) {
        entity.id = existingEntityId
    }

    @Query("select * from feats order by name")
    protected abstract fun getAllSortedByNameInternal(): List<FeatEntity>

    override fun getAllSortedByName(): List<Feat> =
        getAllSortedByNameInternal().map { it.toCoreModel() }

    @Query("select * from feats where id = :featId")
    protected abstract fun getByIdInternal(featId: Long): FeatEntity

    override fun getById(featId: Long): Feat =
        getByIdInternal(featId).toCoreModel()
}