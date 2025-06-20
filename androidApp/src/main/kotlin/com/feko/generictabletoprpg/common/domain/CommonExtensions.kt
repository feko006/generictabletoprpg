package com.feko.generictabletoprpg.common.domain

fun Number.asSignedString() = "${if (toInt() >= 0) "+" else ""}$this"
