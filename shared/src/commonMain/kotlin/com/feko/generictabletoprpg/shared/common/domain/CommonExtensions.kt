package com.feko.generictabletoprpg.shared.common.domain

fun Number.asSignedString() = "${if (toInt() >= 0) "+" else ""}$this"
