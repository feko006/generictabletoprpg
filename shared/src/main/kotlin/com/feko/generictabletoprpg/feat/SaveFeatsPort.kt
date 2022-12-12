package com.feko.generictabletoprpg.feat

interface SaveFeatsPort {
    fun save(feats: List<Feat>): Result<Boolean>
}