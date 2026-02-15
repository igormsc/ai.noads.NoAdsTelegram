package ai.noads.telegram.hooks;

import static ai.noads.telegram.helpers.HookHelper.hookMethod;

public class DisableProfileSwipeBack {

    public static void init(ClassLoader cl) {
            hookMethod(cl, "org.telegram.ui.ProfileActivity", "isSwipeBackEnabled", param -> param.setResult(false));
    }
}