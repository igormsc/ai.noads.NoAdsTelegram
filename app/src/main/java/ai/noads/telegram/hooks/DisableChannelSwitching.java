package ai.noads.telegram.hooks;

import de.robv.android.xposed.XposedHelpers;

import static ai.noads.telegram.helpers.HookHelper.hookMethod;

public class DisableChannelSwitching {

    public static void init(ClassLoader classLoader) {
        try {
            Class<?> clazz = XposedHelpers.findClass("org.telegram.ui.ChatPullingDownDrawable", classLoader);
            hookMethod(clazz, "getNextUnreadDialog", param -> param.setResult(null));
            hookMethod(clazz, "drawBottomPanel", param -> param.setResult(null));
            hookMethod(clazz, "draw", param -> param.setResult(null));
            hookMethod(clazz, "needDrawBottomPanel", param -> param.setResult(false));
            hookMethod(clazz, "showBottomPanel", param -> { if (param.args.length > 0) param.args[0] = false; });
        } catch (Throwable ignored) {}
    }


}