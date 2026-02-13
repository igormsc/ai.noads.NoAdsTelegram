package ai.noads.telegram.helpers;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class HookHelper {
    public static void hookMethod(ClassLoader cl, String className, String methodName, HookAction action) {
        try {
            Class<?> clazz = XposedHelpers.findClass(className, cl);
            XposedBridge.hookAllMethods(clazz, methodName, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) { action.execute(param); }
            });
        } catch (Throwable t) {
            XposedBridge.log("HookHelper: failed to hook " + className + "." + methodName);
        }
    }

    public static void hookMethod(Class<?> clazz, String methodName, HookAction action) {
        try {
            XposedBridge.hookAllMethods(clazz, methodName, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) { action.execute(param);} });
        } catch (Throwable t) {
            XposedBridge.log("HookHelper: failed to hook " + methodName);
        }
    }

    @FunctionalInterface
    public interface HookAction {
        void execute(XC_MethodHook.MethodHookParam param);
    }
}