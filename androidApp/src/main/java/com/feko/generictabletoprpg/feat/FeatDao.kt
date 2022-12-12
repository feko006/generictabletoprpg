package com.feko.generictabletoprpg.feat

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.common.BaseDao
import com.feko.generictabletoprpg.common.Logger

@Dao
abstract class FeatDao
    : BaseDao<FeatEntity>(),
    SaveFeatsPort,
    GetAllFeatsPort {

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
    abstract override fun getAllSortedByName(): List<Feat>
}