package com.feko.generictabletoprpg.features.weapon

import com.feko.generictabletoprpg.features.weapon.ui.WeaponDetailsViewModel
import com.feko.generictabletoprpg.shared.common.data.local.GenericTabletopRpgDatabase
import com.feko.generictabletoprpg.shared.features.weapon.WeaponDao
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val weaponModule = module {
    single { get<GenericTabletopRpgDatabase>().weaponDao() }
    viewModel { WeaponDetailsViewModel(get<WeaponDao>()) }
}
