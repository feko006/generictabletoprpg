package com.feko.generictabletoprpg.com.feko.generictabletoprpg

fun Number.asSignedString() = "${if (toInt() >= 0) "+" else ""}$this"