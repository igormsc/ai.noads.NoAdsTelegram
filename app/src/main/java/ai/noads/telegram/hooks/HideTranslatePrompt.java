package ai.noads.telegram.hooks;

import ai.noads.telegram.helpers.HookHelper;

public class HideTranslatePrompt {

    public static void init(ClassLoader cl) {
        try {
            HookHelper.hookMethod(cl, "org.telegram.messenger.TranslateController", "isTranslateDialogHidden",  param -> {param.setResult(true);});
        } catch (Throwable ignored) {}
    }
}
