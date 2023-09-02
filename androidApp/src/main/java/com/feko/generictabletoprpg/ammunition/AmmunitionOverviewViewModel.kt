package com.feko.generictabletoprpg.ammunition

import com.feko.generictabletoprpg.common.IGetAll
import com.feko.generictabletoprpg.common.OverviewViewModel

class AmmunitionOverviewViewModel(
    getAll: IGetAll<Ammunition>
) : OverviewViewModel<Ammunition>(getAll)