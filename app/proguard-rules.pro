# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.
-keep class com.couchbase.lite.** {*;}
-keep class com.fasterxml.jackson.databind.** {*;}
-keep class reach52.marketplace.community.models.** {*;}
-keep class kotlin.reflect.** { *; }

#Kotlinx serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class reach52.marketplace.community.**$$serializer { *; }
-keepclassmembers class reach52.marketplace.community.** {
    *** Companion;
}
-keepclasseswithmembers class reach52.marketplace.community.** {
    kotlinx.serialization.KSerializer serializer(...);
}