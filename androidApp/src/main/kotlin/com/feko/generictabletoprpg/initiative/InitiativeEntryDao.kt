package com.feko.generictabletoprpg.initiative

import androidx.room.Dao
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
}