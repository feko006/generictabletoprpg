package com.feko.generictabletoprpg.shared

import com.feko.generictabletoprpg.shared.common.data.ParseEdnAsMapEdnJava
import com.feko.generictabletoprpg.shared.common.data.ProcessEdnMapEdnJava
import com.feko.generictabletoprpg.shared.common.data.UserPreferences
import com.feko.generictabletoprpg.shared.common.domain.IParseEdnAsMap
import com.feko.generictabletoprpg.shared.common.domain.IProcessEdnMap
import com.feko.generictabletoprpg.shared.common.domain.IUserPreferences
import com.feko.generictabletoprpg.shared.features.action.actionModule
import com.feko.generictabletoprpg.shared.features.ammunition.ammunitionModule
import com.feko.generictabletoprpg.shared.features.armor.armorModule
import com.feko.generictabletoprpg.shared.features.basecontent.baseContentModule
import com.feko.generictabletoprpg.shared.features.condition.conditionModule
import com.feko.generictabletoprpg.shared.features.disease.diseaseModule
import com.feko.generictabletoprpg.shared.features.encounter.encounterModule
import com.feko.generictabletoprpg.shared.features.feat.featModule
import com.feko.generictabletoprpg.shared.features.filter.filterModule
import com.feko.generictabletoprpg.shared.features.io.ioModule
import com.feko.generictabletoprpg.shared.features.searchall.searchAllModule
import com.feko.generictabletoprpg.shared.features.spell.spellModule
import com.feko.generictabletoprpg.shared.features.tracker.trackerGroupsModule
import com.feko.generictabletoprpg.shared.features.tracker.trackerModule
import com.feko.generictabletoprpg.shared.features.weapon.weaponModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule = module {
    // Services
    single { getRoomDatabase() }

    singleOf(::ParseEdnAsMapEdnJava) bind IParseEdnAsMap::class
    singleOf(::ProcessEdnMapEdnJava) bind IProcessEdnMap::class
    singleOf(::UserPreferences) bind IUserPreferences::class

    // VMs
    // TODO KMP
//    viewModelOf(::AppViewModel)
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