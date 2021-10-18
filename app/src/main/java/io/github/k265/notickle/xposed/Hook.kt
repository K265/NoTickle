package io.github.k265.notickle.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Hook : IXposedHookLoadPackage {

    companion object {
        const val TAG = "io.github.k265.notickle"
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam == null) {
            return
        }

        val packageName = lpparam.packageName
        if (packageName != "com.tencent.mm") {
            return
        }

        XposedBridge.log("$TAG: hooking $packageName")

        XposedHelpers.findAndHookMethod(
            "com.tencent.mm.plugin.patmsg.PluginPatMsg",
            lpparam.classLoader,
            "isPatEnable",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    if (param == null) {
                        return
                    }

                    param.result = false
                }
            }
        )
    }

}