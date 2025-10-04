package com.feko.generictabletoprpg.features.io

import com.feko.generictabletoprpg.features.io.ui.ImportViewModel
import com.feko.generictabletoprpg.shared.features.action.ActionDao
import com.feko.generictabletoprpg.shared.features.ammunition.AmmunitionDao
import com.feko.generictabletoprpg.shared.features.armor.ArmorDao
import com.feko.generictabletoprpg.shared.features.condition.ConditionDao
import com.feko.generictabletoprpg.shared.features.disease.DiseaseDao
import com.feko.generictabletoprpg.shared.features.feat.FeatDao
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IImportAllUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IJsonImportAllUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IOrcbrewImportAllUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IOrcbrewImportAmmunitionsUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IOrcbrewImportArmorsUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IOrcbrewImportFeatsUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IOrcbrewImportSpellsUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.IOrcbrewImportWeaponsUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.ImportAllUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.JsonImportAllUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.OrcbrewImportAllUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.OrcbrewImportAmmunitionsUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.OrcbrewImportArmorsUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.OrcbrewImportFeatsUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.OrcbrewImportSpellsUseCase
import com.feko.generictabletoprpg.shared.features.io.domain.usecase.OrcbrewImportWeaponsUseCase
import com.feko.generictabletoprpg.shared.features.spell.SpellDao
import com.feko.generictabletoprpg.shared.features.tracker.TrackedThingDao
import com.feko.generictabletoprpg.shared.features.tracker.TrackedThingGroupDao
import com.feko.generictabletoprpg.shared.features.weapon.WeaponDao
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val ioModule = module {
    single<IOrcbrewImportSpellsUseCase> {
        OrcbrewImportSpellsUseCase(
            get(),
            get<SpellDao>()
        )
    }
    single<IOrcbrewImportFeatsUseCase> {
        OrcbrewImportFeatsUseCase(
            get(),
            get<FeatDao>()
        )
    }
    single<IOrcbrewImportWeaponsUseCase> {
        OrcbrewImportWeaponsUseCase(
            get(),
            get<WeaponDao>()
        )
    }
    single<IOrcbrewImportAmmunitionsUseCase> {
        OrcbrewImportAmmunitionsUseCase(
            get(),
            get<AmmunitionDao>()
        )
    }
    single<IOrcbrewImportArmorsUseCase> {
        OrcbrewImportArmorsUseCase(
            get(),
            get<ArmorDao>()
        )
    }
    singleOf(::OrcbrewImportAllUseCase) bind IOrcbrewImportAllUseCase::class
    single<IJsonImportAllUseCase> {
        JsonImportAllUseCase(
            get<ActionDao>(),
            get<ConditionDao>(),
            get<DiseaseDao>(),
            get<TrackedThingGroupDao>(),
            get<TrackedThingDao>()
        )
    }
    singleOf(::ImportAllUseCase) bind IImportAllUseCase::class
    viewModelOf(::ImportViewModel)
}
