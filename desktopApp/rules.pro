-dontpreverify

-dontwarn ch.qos.logback.**
-dontwarn com.oracle.svm.core.annotate.**
-dontwarn io.github.oshai.kotlinlogging.**
-dontwarn kotlinx.coroutines.slf4j.**
-dontwarn org.slf4j.**

-dontnote org.slf4j.**
-dontnote kotlinx.serialization.**

-keep @com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate public class * { *; }
-keep class * extends androidx.room.RoomDatabase { <init>(); }
-keep class io.github.oshai.kotlinlogging.logback.** { *; }
-keep class io.github.oshai.kotlinlogging.logback.internal.** { *; }