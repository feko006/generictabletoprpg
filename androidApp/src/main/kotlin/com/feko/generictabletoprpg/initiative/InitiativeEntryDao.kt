package com.feko.generictabletoprpg.initiative

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.initiative.InitiativeEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InitiativeEntryDao {

    @Query("select * from initiative_entries order by initiative desc")
    fun getAllSortedByInitiative(): Flow<List<InitiativeEntryEntity>>

    @Insert
    suspend fun insert(entity: InitiativeEntryEntity): Long

    @Update
    suspend fun update(entity: InitiativeEntryEntity)

    @Delete
    suspend fun delete(entity: InitiativeEntryEntity)

    @Query("delete from initiative_entries where keepOnRefresh = 0")
    suspend fun resetInitiative()

    @Query("update initiative_entries set availableLegendaryActions = legendaryActions")
    suspend fun resetAvailableLegendaryActions()

    @Query("update initiative_entries set hasTurn = (id = :entityId)")
    suspend fun setCurrentTurn(entityId: Long)

    @Query("update initiative_entries set isTurnCompleted = (id = :entityId)")
    suspend fun setTurnCompleted(entityId: Long)
}