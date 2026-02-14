package ai.noads.telegram.hooks;

import java.util.ArrayList;

import static ai.noads.telegram.helpers.HookHelper.hookMethod;

public class DisableSponsoredMessages {

    public static void init(ClassLoader cl) {
        hookMethod(cl, "org.telegram.ui.ChatActivity", "addSponsoredMessages", param -> param.setResult(null));
        hookMethod(cl, "org.telegram.messenger.MessagesController", "getSponsoredMessages", param -> param.setResult(new ArrayList<>()));
        hookMethod(cl, "org.telegram.messenger.MessagesController", "getSponsoredMessagesCount", param -> param.setResult(0));
    }


}