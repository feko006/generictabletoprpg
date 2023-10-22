package com.feko.generictabletoprpg.ammunition

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById

class AmmunitionDetailsViewModel(
    getById: IGetById<Ammunition>
) : DetailsViewModel<Ammunition>(getById)