package com.feko.generictabletoprpg.shared.features.action.ui

import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.shared.features.action.Action

class ActionDetailsViewModel(getById: IGetByIdDao<Action>) : DetailsViewModel<Action>(getById)