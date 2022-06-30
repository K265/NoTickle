package io.github.k265.notickle.xposed

import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

class Helper {
    companion object {
        fun findAndHookMethod(
            className: String,
            classLoader: ClassLoader?,
            methodName: String,
            vararg parameterTypesAndCallback: Any?
        ) {
            try {
                XposedHelpers.findAndHookMethod(
                    className,
                    classLoader,
                    methodName,
                    *parameterTypesAndCallback
                )
            } catch (t: Throwable) {
                XposedBridge.log("failed to hook $methodName in $className")
                XposedBridge.log(t)
            }
        }
    }
}