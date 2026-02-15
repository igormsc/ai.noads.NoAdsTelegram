package ai.noads.telegram.hooks;

import de.robv.android.xposed.XposedHelpers;

import static ai.noads.telegram.helpers.HookHelper.hookMethod;

public class DisableChannelSwitching {

    public static void init(ClassLoader classLoader) {
        try {
            Class<?> cl = XposedHelpers.findClass("org.telegram.ui.ChatPullingDownDrawable", classLoader);
            hookMethod(cl, "getNextUnreadDialog", param -> param.setResult(null));
            hookMethod(cl, "drawBottomPanel", param -> param.setResult(null));
            hookMethod(cl, "draw", param -> param.setResult(null));
            hookMethod(cl, "needDrawBottomPanel", param -> param.setResult(false));
            hookMethod(cl, "showBottomPanel", param -> { if (param.args.length > 0) param.args[0] = false; });
        } catch (Throwable ignored) {}
    }


}