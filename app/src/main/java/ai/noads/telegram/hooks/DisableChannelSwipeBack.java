package ai.noads.telegram.hooks;

import de.robv.android.xposed.XposedHelpers;

import static ai.noads.telegram.helpers.HookHelper.hookMethod;


public class DisableChannelSwipeBack {

    public static void init(ClassLoader cl) {
        hookMethod(cl, "org.telegram.ui.ChatActivity", "isSwipeBackEnabled", param -> {
                Object chat = XposedHelpers.getObjectField(param.thisObject, "currentChat");
                if (chat != null) param.setResult(false);
        });
    }
}

