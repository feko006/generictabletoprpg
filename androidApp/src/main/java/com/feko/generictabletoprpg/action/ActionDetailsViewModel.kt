package com.feko.generictabletoprpg.action

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById

class ActionDetailsViewModel(getById: IGetById<Action>) : DetailsViewModel<Action>(getById)