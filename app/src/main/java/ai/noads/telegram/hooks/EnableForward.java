package ai.noads.telegram.hooks;

import static ai.noads.telegram.helpers.HookHelper.hookMethod;

public class EnableForward {

    public static void init(ClassLoader cl) {
        hookMethod(cl, "org.telegram.messenger.MessagesController", "isChatNoForwards", param -> param.setResult(false));
        hookMethod(cl, "org.telegram.ui.ChatActivity", "hasSelectedNoforwardsMessage", param -> param.setResult(false));
        hookMethod(cl, "org.telegram.messenger.MessageObject", "canForwardMessage", param -> param.setResult(true));
    }

}
