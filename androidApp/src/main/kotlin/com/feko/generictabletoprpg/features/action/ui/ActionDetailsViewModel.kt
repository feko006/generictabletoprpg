package com.feko.generictabletoprpg.features.action.ui

import com.feko.generictabletoprpg.features.action.Action
import com.feko.generictabletoprpg.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao

class ActionDetailsViewModel(getById: IGetByIdDao<Action>) : DetailsViewModel<Action>(getById)