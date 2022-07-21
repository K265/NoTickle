package io.github.k265.notickle.xposed

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class Hook : IXposedHookLoadPackage {

    companion object {
        const val TAG = Helper.TAG
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam == null) {
            return
        }

        val packageName = lpparam.packageName
        if (packageName != "com.tencent.mm") {
            return
        }

        val processName = lpparam.processName
        if (processName != "com.tencent.mm") {
            return
        }

        XposedBridge.log("$TAG: hooking $packageName")

        when (val versionName = Helper.getVersionName(packageName)) {
            "8.0.18" -> {
                hookSendPat(lpparam, "N")
            }
            "8.0.19" -> {
                hookSendPat(lpparam, "Q")
            }
            "8.0.21" -> {
                hookSendPat(lpparam, "Q")
            }
            "8.0.24" -> {
                hookSendPat(lpparam, "aa")
            }
            else -> {
                hookIsPatEnable(lpparam)
                XposedBridge.log("$TAG: unknown $packageName versionName: $versionName, applying isPatEnable hook")
            }
        }

    }

    private fun hookSendPat(lpparam: XC_LoadPackage.LoadPackageParam, methodName: String) {
        Helper.findAndHookMethod(
            "com.tencent.mm.plugin.patmsg.a",
            lpparam.classLoader,
            methodName,
            Int::class.javaPrimitiveType,
            "java.lang.String",
            "java.lang.String",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    XposedBridge.log("$TAG: hooking com.tencent.mm.plugin.patmsg.ui.a.$methodName")
                    return null
                }
            }
        )
    }

    /**
     * hook isPatEnable to return false
     * this will disable tickle reception too
     */
    private fun hookIsPatEnable(lpparam: XC_LoadPackage.LoadPackageParam) {
        Helper.findAndHookMethod(
            "com.tencent.mm.plugin.patmsg.PluginPatMsg",
            lpparam.classLoader,
            "isPatEnable",
            object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                    XposedBridge.log("$TAG: hooking com.tencent.mm.plugin.patmsg.PluginPatMsg.isPatEnable")
                    return false
                }
            }
        )
    }

}