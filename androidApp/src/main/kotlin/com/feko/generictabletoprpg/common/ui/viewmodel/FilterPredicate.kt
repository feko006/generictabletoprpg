package com.feko.generictabletoprpg.common.ui.viewmodel

import com.feko.generictabletoprpg.features.filter.Filter

class FilterPredicate(val filter: Filter?) : (Any) -> Boolean {
    override fun invoke(item: Any): Boolean {
        if (filter == null) {
            return true
        }

        return filter.isAccepted(item)
    }
}
