package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.spells.Spell

interface SaveSpellsPort : (List<Spell>) -> Result<Boolean>