package com.feko.generictabletoprpg.ammunition

import com.feko.generictabletoprpg.common.Cost
import com.feko.generictabletoprpg.common.FromSource
import com.feko.generictabletoprpg.common.Identifiable
import com.feko.generictabletoprpg.common.Named

data class Ammunition(
    override val id: Long = 0,
    override val name: String,
    val sellQuantity: Long,
    val cost: Cost,
    val weight: String,
    override var source: String = ""
) : Identifiable,
    Named,
    FromSource