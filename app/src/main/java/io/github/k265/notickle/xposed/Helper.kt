package io.github.k265.notickle.xposed

import android.content.Context
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

fun String.findClassOrNull(classLoader: ClassLoader?): Class<*>? =
    XposedHelpers.findClassIfExists(this, classLoader)

fun Class<*>.callStaticMethod(methodName: String?, vararg args: Any?): Any? =
    XposedHelpers.callStaticMethod(this, methodName, *args)

fun <T> Any.callMethodAs(methodName: String?, vararg args: Any?) =
    XposedHelpers.callMethod(this, methodName, *args) as T


class Helper {
    companion object {
        const val TAG = "io.github.k265.notickle"

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
                XposedBridge.log("$TAG: failed to hook $methodName in $className")
                XposedBridge.log(t)
            }
        }

        fun getVersionName(packageName: String): String {
            var versionName = ""
            try {
                val ctx: Context = getContext()
                val info = ctx.packageManager.getPackageInfo(packageName, 0)
                versionName = info.versionName
                val versionCode = info.versionCode
                XposedBridge.log("$TAG: $packageName versionName: $versionName, versionCode: $versionCode")
            } catch (t: Throwable) {
                XposedBridge.log("$TAG: failed to get version info")
                XposedBridge.log(t)
            }
            return versionName
        }

        private fun getContext(): Context {
            val activityThread = "android.app.ActivityThread".findClassOrNull(null)
                ?.callStaticMethod("currentActivityThread")!!
            return activityThread.callMethodAs("getSystemContext")
        }

    }
}