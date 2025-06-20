package com.feko.generictabletoprpg.features.weapon

import com.feko.generictabletoprpg.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.features.weapon.ui.WeaponDetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val weaponModule = module {
    single { get<GenericTabletopRpgDatabase>().weaponDao() }
    viewModel { WeaponDetailsViewModel(get<WeaponDao>()) }
}
