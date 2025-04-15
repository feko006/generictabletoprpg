package com.feko.generictabletoprpg.initiative

import androidx.room.Dao
import androidx.room.Query
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.initiative.InitiativeEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InitiativeEntryDao {
    @Query("select * from initiative_entries order by initiative desc")
    fun getAllSortedByInitiative(): Flow<List<InitiativeEntryEntity>>
}