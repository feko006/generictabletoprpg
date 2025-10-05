package com.feko.generictabletoprpg

import com.feko.generictabletoprpg.common.data.ParseEdnAsMapEdnJava
import com.feko.generictabletoprpg.common.data.ProcessEdnMapEdnJava
import com.feko.generictabletoprpg.common.data.UserPreferences
import com.feko.generictabletoprpg.common.ui.viewmodel.AppViewModel
import com.feko.generictabletoprpg.features.action.actionModule
import com.feko.generictabletoprpg.features.ammunition.ammunitionModule
import com.feko.generictabletoprpg.features.armor.armorModule
import com.feko.generictabletoprpg.features.basecontent.baseContentModule
import com.feko.generictabletoprpg.features.condition.conditionModule
import com.feko.generictabletoprpg.features.disease.diseaseModule
import com.feko.generictabletoprpg.features.encounter.encounterModule
import com.feko.generictabletoprpg.features.feat.featModule
import com.feko.generictabletoprpg.features.filter.filterModule
import com.feko.generictabletoprpg.features.io.ioModule
import com.feko.generictabletoprpg.features.searchall.searchAllModule
import com.feko.generictabletoprpg.features.spell.spellModule
import com.feko.generictabletoprpg.features.tracker.trackerGroupsModule
import com.feko.generictabletoprpg.features.tracker.trackerModule
import com.feko.generictabletoprpg.features.weapon.weaponModule
import com.feko.generictabletoprpg.shared.common.domain.IParseEdnAsMap
import com.feko.generictabletoprpg.shared.common.domain.IProcessEdnMap
import com.feko.generictabletoprpg.shared.common.domain.IUserPreferences
import com.feko.generictabletoprpg.shared.getDatabaseBuilder
import com.feko.generictabletoprpg.shared.getRoomDatabase
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule = module {
    // Services
    single { getRoomDatabase(getDatabaseBuilder(get())) }

    singleOf(::ParseEdnAsMapEdnJava) bind IParseEdnAsMap::class
    singleOf(::ProcessEdnMapEdnJava) bind IProcessEdnMap::class
    singleOf(::UserPreferences) bind IUserPreferences::class

    // VMs
    viewModelOf(::AppViewModel)
}


val diModules = listOf(
    commonModule,
    spellModule,
    featModule,
    actionModule,
    conditionModule,
    diseaseModule,
    weaponModule,
    ammunitionModule,
    armorModule,
    ioModule,
    trackerGroupsModule,
    trackerModule,
    searchAllModule,
    filterModule,
    encounterModule,
    baseContentModule
)
