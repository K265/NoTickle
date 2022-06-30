package io.github.k265.notickle.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
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

        Helper.findAndHookMethod(
            "com.tencent.mm.plugin.patmsg.a",
            lpparam.classLoader,
            "N",
            Int::class.javaPrimitiveType,
            "java.lang.String",
            "java.lang.String",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    if (param == null) {
                        return
                    }

                    param.result = null
                    XposedBridge.log("$TAG: hooking $packageName: com.tencent.mm.plugin.patmsg.a.N")
                }
            }
        )

        // Helper.findAndHookMethod(
        //     "com.tencent.mm.plugin.patmsg.PluginPatMsg",
        //     lpparam.classLoader,
        //     "isPatEnable",
        //     object : XC_MethodHook() {
        //         override fun beforeHookedMethod(param: MethodHookParam?) {
        //             if (param == null) {
        //                 return
        //             }
        //
        //             param.result = false
        //             XposedBridge.log("$TAG: hooking $packageName: com.tencent.mm.plugin.patmsg.PluginPatMsg.isPatEnable")
        //         }
        //     }
        // )
    }

}