package com.feko.generictabletoprpg.features.searchall

import com.feko.generictabletoprpg.features.searchall.ui.SearchAllViewModel
import com.feko.generictabletoprpg.shared.features.action.ActionDao
import com.feko.generictabletoprpg.shared.features.ammunition.AmmunitionDao
import com.feko.generictabletoprpg.shared.features.armor.ArmorDao
import com.feko.generictabletoprpg.shared.features.condition.ConditionDao
import com.feko.generictabletoprpg.shared.features.disease.DiseaseDao
import com.feko.generictabletoprpg.shared.features.feat.FeatDao
import com.feko.generictabletoprpg.shared.features.searchall.usecase.ISearchAllUseCase
import com.feko.generictabletoprpg.shared.features.searchall.usecase.SearchAllUseCase
import com.feko.generictabletoprpg.shared.features.spell.SpellDao
import com.feko.generictabletoprpg.shared.features.weapon.WeaponDao
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
