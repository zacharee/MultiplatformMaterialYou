package dev.zwander.compose.util

import androidx.compose.ui.graphics.Color
import korlibs.ffi.osx.NSClass
import korlibs.ffi.osx.NSObject
import korlibs.ffi.osx.NSString
import korlibs.ffi.osx.ObjcRef

class UserDefaults(id: Long) : NSObject(id) {
    @Suppress("MemberVisibilityCanBePrivate")
    fun objectForKey(key: String): String? =
        ObjcRef(msgSend("objectForKey:", NSString(key).ref.id))
            .msgSend("description")
            .takeIf { it != 0L }
            ?.let { NSString(it).cString }

    fun getAccentColor(): Color {
        return macOsColorKeyToColor(objectForKey("AppleAccentColor")?.toIntOrNull())
    }

    companion object : NSClass("NSUserDefaults") {
        fun standardUserDefaults(): UserDefaults =
            UserDefaults(OBJ_CLASS.msgSend("standardUserDefaults"))
    }
}
