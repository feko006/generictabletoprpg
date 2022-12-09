package com.feko.generictabletoprpg.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateMap

@Composable
fun rememberSaveableMutableStateIntToIntMap(keyPrefix: String): SnapshotStateMap<Int, Int> {
    return rememberSaveable(
        saver = mapSaver(
            save = { map ->
                val mapToSave = mutableMapOf<String, Int>()
                map.forEach {
                    mapToSave["$keyPrefix${it.key}"] = it.value
                }
                mapToSave
            },
            restore = { map ->
                val pairs = arrayListOf<Pair<Int, Int>>()
                map.forEach {
                    pairs.add(
                        Pair(
                            it.key.replace(keyPrefix, "").toInt(),
                            it.value as Int
                        )
                    )
                }
                mutableStateMapOf(*pairs.toTypedArray())
            }
        )
    ) { mutableStateMapOf() }
}