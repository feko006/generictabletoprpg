package com.feko.generictabletoprpg.features.condition.ui

import com.feko.generictabletoprpg.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.features.condition.Condition

class ConditionDetailsViewModel(getById: IGetByIdDao<Condition>) : DetailsViewModel<Condition>(getById)