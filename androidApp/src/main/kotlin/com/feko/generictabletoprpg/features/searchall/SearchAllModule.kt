package com.feko.generictabletoprpg.features.searchall

import com.feko.generictabletoprpg.features.action.ActionDao
import com.feko.generictabletoprpg.features.ammunition.AmmunitionDao
import com.feko.generictabletoprpg.features.armor.ArmorDao
import com.feko.generictabletoprpg.features.condition.ConditionDao
import com.feko.generictabletoprpg.features.disease.DiseaseDao
import com.feko.generictabletoprpg.features.feat.FeatDao
import com.feko.generictabletoprpg.features.searchall.domain.usecase.ISearchAllUseCase
import com.feko.generictabletoprpg.features.searchall.domain.usecase.SearchAllUseCase
import com.feko.generictabletoprpg.features.searchall.ui.SearchAllViewModel
import com.feko.generictabletoprpg.features.spell.SpellDao
import com.feko.generictabletoprpg.features.weapon.WeaponDao
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val searchAllModule = module {
    single<ISearchAllUseCase> {
        SearchAllUseCase(
            listOf(
                get<ActionDao>(),
                get<AmmunitionDao>(),
                get<ArmorDao>(),
                get<ConditionDao>(),
                get<DiseaseDao>(),
                get<FeatDao>(),
                get<SpellDao>(),
                get<WeaponDao>(),
            )
        )
    }
    viewModel { params -> SearchAllViewModel(params.getOrNull(), get()) }
}
