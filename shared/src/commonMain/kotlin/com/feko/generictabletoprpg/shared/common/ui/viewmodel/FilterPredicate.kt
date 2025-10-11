package com.feko.generictabletoprpg.shared.common.ui.viewmodel

import com.feko.generictabletoprpg.shared.features.filter.Filter

class FilterPredicate(val filter: Filter?) : (Any) -> Boolean {
    override fun invoke(item: Any): Boolean {
        if (filter == null) {
            return true
        }

        return filter.isAccepted(item)
    }
}
